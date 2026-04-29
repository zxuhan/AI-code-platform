# ai-code

<p align="center">
  <img alt="Java"            src="https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white">
  <img alt="Spring Boot"     src="https://img.shields.io/badge/Spring%20Boot-3.5.4-6DB33F?logo=springboot&logoColor=white">
  <img alt="LangChain4j"     src="https://img.shields.io/badge/LangChain4j-1.13.1-1C3C3C">
  <img alt="Gemini"          src="https://img.shields.io/badge/LLM-Gemini%202.5-4285F4?logo=google&logoColor=white">
  <img alt="Vue"             src="https://img.shields.io/badge/Vue-3.5-4FC08D?logo=vuedotjs&logoColor=white">
  <img alt="TypeScript"      src="https://img.shields.io/badge/TypeScript-5.8-3178C6?logo=typescript&logoColor=white">
  <img alt="Docker"          src="https://img.shields.io/badge/Docker-ready-2496ED?logo=docker&logoColor=white">
  <img alt="License"         src="https://img.shields.io/badge/License-MIT-blue">
</p>

> Type a sentence. Get a working web app: a single-page HTML, a multi-file site, or a complete Vue project scaffolded by a tool-calling agent that writes its own files. Output streams back in real time, gets parsed and saved, headless Chromium snaps a cover, and the bytes ship to object storage as a sharable URL. The router picks the cheapest model that can answer each prompt, per-app chat memory replays from MySQL when the cache evicts, and the whole thing fits in a 768 MB JVM heap. Spring Boot on the back, Vue 3 on the front.

## Try it live

**<https://ai-code.zxuhan.me/>**

Sign up with any account. No payment, no quota. Admin role unlocks the user, app, and chat-history dashboards under `/admin/*`.

---

## Architecture

```mermaid
flowchart TB
    P([prompt]) --> R{Routing LLM<br/>gemini-2.5-flash-lite}

    R -->|HTML mode| H[Single-page generator<br/>gemini-2.5-flash · streamed]
    R -->|multi-file mode| M[Multi-file generator<br/>gemini-2.5-flash · streamed]
    R -->|Vue project mode| V[Vue agent<br/>gemini-2.5-pro · tool-calling]

    V -.invokes.-> T[(File tools:<br/>read · write · modify · delete · ls)]

    H --> S[CodeFileSaver]
    M --> S
    V --> B[VueProjectBuilder<br/>npm ci + npm run build]
    B --> S

    S --> SC[Headless Chromium<br/>cover screenshot]
    SC --> R2[(Cloudflare R2)]
    R2 --> URL([deployable URL])
```

Every request enters with one sentence. The router model is the cheapest one Google ships, so classifying a prompt costs almost nothing. Real generation goes to a flash model for raw text, or a pro model for the tool-calling agent. The result lands on disk, gets a screenshot taken, and ships to R2. Cost per request stays flat: one cheap routing call plus one streaming generation. Only the Vue mode pays for the bigger reasoning model, and only when the user actually wants a project.

---

## The generators

| Mode | Model | What it does | Output |
|------|-------|--------------|--------|
| `HTML` | gemini-2.5-flash | Streams a single self-contained `.html` file with inline CSS and JS. Parsed by `HtmlCodeParser`. | One file |
| `MULTI_FILE` | gemini-2.5-flash | Streams `index.html`, `style.css`, `script.js` as labeled blocks. `MultiFileCodeParser` splits on the markers. | Three files |
| `VUE_PROJECT` | gemini-2.5-pro | Agent scaffolds a complete Vue 3 project (components, router, stores, build config) by repeatedly invoking `FileWriteTool`, `FileModifyTool`, etc. `VueProjectBuilder` then runs `npm ci && npm run build`. | Full project plus built `dist/` |

The mode is chosen by `AiCodeGenTypeRoutingService`, a separate `@AiService` backed by the lite model. Users can override it per app from the UI.

---

## Tool-calling Vue scaffolding

Vue projects don't fit one streaming response. They need many files written in coordinated order, with cross-references that have to stay consistent across calls. Asking the model to dump a whole project as text means truncation, broken imports, and no recovery once it gets confused. The Vue agent gets a toolbelt instead and works the project incrementally, keeping its own state on disk.

| Tool | Spec | Used for |
|------|------|---------|
| `FileWriteTool` | `(relativePath, content)` | New file creation |
| `FileModifyTool` | `(relativePath, oldText, newText)` | Surgical edits |
| `FileDeleteTool` | `(relativePath)` | Cleanup of intermediate files |
| `FileReadTool` | `(relativePath)` | Inspect previous output |
| `FileDirReadTool` | `(relativeDir)` | List directory contents |

Each tool extends `BaseTool`. Paths are validated against the per-app working directory so the model can't traverse out, and results come back structured so the agent can reason over partial state. `ToolManager` wires the set into the `AiServices` builder. When the model hallucinates a tool name, `hallucinatedToolNameStrategy` returns a clean error so the run keeps going instead of crashing.

The agent finishes when it stops emitting tool calls. `VueProjectBuilder` then runs `npm ci && npm run build`, captures stderr on failure, and surfaces a buildable `dist/`.

---

## Streaming over SSE

Two output paths share one SSE channel. `Flux<String>` for raw text (HTML and multi-file modes), `TokenStream` for tool-aware sessions (Vue mode). The bridge handles three things the wire format can't tolerate: a hallucinated tool name midway, partial tool arguments arriving as JSON fragments before the full call exists, and a client disconnect that should release every server-side resource on the way out.

For raw text, `processCodeStream` taps the `Flux` with `doOnNext` to accumulate chunks and `doOnComplete` to parse and save once the stream ends. Bytes pass through to the client untouched.

For tool-aware sessions, `processTokenStream` wraps the `TokenStream` in `Flux.create` and routes each callback to a typed JSON message:

- `onPartialResponse` to `AiResponseMessage` (the agent's reasoning text)
- `onPartialToolCall` to `ToolRequestMessage` (tool args streaming in as fragments)
- `onToolExecuted` to `ToolExecutedMessage`
- `onCompleteResponse` and `onError` terminate the Flux

The client renders raw text inline for HTML mode and switch-renders typed messages for Vue mode, showing tool-call indicators and tool-result panels as the agent works.

---

## Per-app chat memory

Each app id has its own conversation context. The AI service objects are expensive to build because they wire up the model, the tool manager, and the chat-memory store, and they can't be shared across apps without leaking history. The cache solves both:

```text
buildCacheKey(appId, codeGenType)
                │
                ▼
   ┌─────────────────────────────┐
   │  Caffeine LRU                │  size: 1000
   │  AiCodeGeneratorService …    │  expireAfterWrite: 30m
   └─────────────────────────────┘  expireAfterAccess: 10m
                │ miss
                ▼
   ┌─────────────────────────────┐
   │  MessageWindowChatMemory     │  maxMessages: 20
   │  · RedisChatMemoryStore       │  hot store, TTL on Redis
   │  · ChatHistoryService.load… │  cold replay from MySQL
   └─────────────────────────────┘
```

On a cache miss the factory builds a fresh memory window keyed by app id, replays the last twenty messages from the `chat_history` MySQL table into it, and constructs a new AI service. Future requests for the same app reuse the cached service, hit Redis directly, and skip the MySQL replay. A `removalListener` logs evictions so warm-up cost is observable.

---

## Pluggable strategies

Adding a new code-gen mode means adding three classes, not editing a switch in five places. `CodeGenTypeEnum` is the single source of truth, and every executor switches on it once.

| Step | Pattern | Implementations | Dispatcher |
|------|---------|-----------------|-----------|
| Parse stream | Strategy | `HtmlCodeParser`, `MultiFileCodeParser` | `CodeParserExecutor` |
| Save to disk | Template method | `HtmlCodeFileSaverTemplate`, `MultiFileCodeFileSaverTemplate` (extend `CodeFileSaverTemplate`) | `CodeFileSaverExecutor` |
| Stream back to client | Strategy | `SimpleTextStreamHandler` (plain text), `JsonMessageStreamHandler` (typed events) | `StreamHandlerExecutor` |

---

## Auto-screenshot and R2 deploy

The screenshot pipeline runs headless Chromium. `WebDriverManager` would happily auto-download a glibc-linked chromedriver, but the production image is Alpine, so that does the wrong thing. The Dockerfile installs the OS `chromium` and `chromium-chromedriver` packages instead and exposes them via `CHROME_BIN` and `CHROMEDRIVER_PATH`. `WebScreenshotUtils.initChromeDriver` checks both at init time: if set, the bundled Alpine binaries are used; otherwise `WebDriverManager` runs as before for native dev. Backward compatible, container aware, no surprises in CI.

Captures go through Hutool's `ImgUtil.compress` at thirty percent quality, which converts PNG to JPEG and cuts roughly eighty percent of the bytes. `R2Manager` uses AWS SDK v2 with path-style addressing against the Cloudflare R2 endpoint. Object keys are partitioned by date (`screenshots/yyyy/MM/dd/<uuid>_compressed.jpg`) so listing stays cheap as the bucket grows.

`ProjectDownloadServiceImpl` runs the same flow for the whole built artifact, zipped on demand.

---

## Tech stack

| Layer    | Stack |
|----------|-------|
| Backend  | Java 21 · Spring Boot · LangChain4j · Spring Session Redis · MyBatis-Flex · AWS SDK v2 · Selenium · Hutool · Knife4j · Lombok |
| LLM      | Google Gemini: Flash-Lite for routing, Flash for streaming text, Pro for the Vue agent |
| Storage  | MySQL · Redis (sessions plus chat memory) · Cloudflare R2 (S3-compatible, screenshots and generated apps) |
| Frontend | Vue 3 · TypeScript · Vite · Pinia · Vue Router · Ant Design Vue · Axios · markdown-it with highlight.js |
| Infra    | Alpine multi-stage Docker images with bundled Chromium · Docker Compose (local 4-service stack; prod backend plus frontend joining a sibling project's network) · GitHub Actions: build, push to Docker Hub, SSH deploy |

---

## Run it locally

### Docker Compose (recommended)

```bash
cp .env.example .env
# fill in: GEMINI_API_KEY (required)
#         R2_* (optional, only the screenshot upload uses them)
./start.sh                        # or: docker compose up -d --build
```

| Service     | URL                                      |
|-------------|------------------------------------------|
| Frontend    | <http://127.0.0.1:8082>                  |
| Backend API | <http://127.0.0.1:8124/api>              |
| API docs    | <http://127.0.0.1:8124/api/doc.html>     |

MySQL and Redis stay on the internal Docker network. `start.sh` waits for every service to report healthy and prints the URLs.

### Local dev (no Docker)

```bash
# backend
cp src/main/resources/application-local.yml.example src/main/resources/application-local.yml
# fill in API keys
mvn spring-boot:run

# frontend (another shell)
cd frontend && npm install && npm run dev
```

Backend on `:8123/api`, frontend on `:5173`. Vite proxies `/api` to the backend.

---

## Project layout

```
src/main/java/com/zxuhan/aicode/
├── ai/
│   ├── AiCodeGenTypeRoutingService.java          # routes prompts to a code-gen mode
│   ├── AiCodeGeneratorService.java               # @AiService, 3 generators (HTML / multi-file / Vue)
│   ├── *ServiceFactory.java                      # builds and caches AiService instances per app
│   ├── tools/                                    # FileWrite / Modify / Delete / Read / DirRead + ToolManager
│   └── model/                                    # HtmlCodeResult, MultiFileCodeResult, message DTOs
├── core/
│   ├── AiCodeGeneratorFacade.java                # entry point: generateAndSaveCodeStream(...)
│   ├── builder/VueProjectBuilder.java            # npm install and npm build for Vue mode
│   ├── parser/                                   # HtmlCodeParser, MultiFileCodeParser, executor
│   ├── saver/                                    # template-method file savers and executor
│   └── handler/                                  # JsonMessage / SimpleText stream handlers
├── service/                                      # App, ChatHistory, Screenshot, ProjectDownload, User
├── controller/                                   # App, ChatHistory, User, Health, StaticResource
├── manager/R2Manager.java                        # S3-compatible upload to Cloudflare R2
├── utils/WebScreenshotUtils.java                 # Selenium plus Chromium screenshots (env-aware for Alpine)
├── annotation/AuthCheck.java                     # @AuthCheck role guard
├── aop/AuthInterceptor.java                      # AOP enforcement
├── config/                                       # CORS, JSON, Redis chat memory, R2, reasoning model
├── exception/                                    # BusinessException, ErrorCode, GlobalExceptionHandler
└── model/{entity,dto,vo,enums}/
sql/                                              # schema (create_table.sql)
frontend/                                         # Vue 3, Vite, Ant Design Vue SPA
.github/workflows/deploy.yml                      # CI: build, push to Docker Hub, SSH deploy
```

---

## License

MIT.
