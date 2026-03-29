# ai-code — Frontend

Vue 3 + Vite + Ant Design Vue. Talks to the ai-code backend.

## Requirements

- Node.js 22+

## Stack

- Vue 3
- Vite
- TypeScript
- Ant Design Vue
- Axios
- Pinia
- Vue Router
- markdown-it + highlight.js (for streaming Markdown render)

## Develop

```bash
# Install dependencies
npm install

# Start dev server (port 5173, proxies /api to localhost:8123)
npm run dev

# Production build
npm run build

# Format
npm run format

# Lint
npm run lint
```

## Generate API client

The backend exposes an OpenAPI spec at `http://localhost:8123/api/v3/api-docs`.
With the backend running, regenerate the typed client:

```bash
npm run openapi2ts
```

## Layout

```
src/
├── api/           # Auto-generated from backend OpenAPI
├── assets/        # Static assets
├── components/    # Shared components (AppCard, MarkdownRenderer, ...)
├── config/        # Runtime config (env.ts)
├── layouts/       # Layout components
├── pages/         # Page components (Home, AppChat, AppEdit, admin, ...)
├── router/        # Route definitions
├── stores/        # Pinia stores (loginUser)
└── utils/         # Helpers (visualEditor, codeGenTypes, time)
```
