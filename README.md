# No Code AI Platform: Spring Boot + LangChain4j

A code generation platform for your deployable web applications using OpenAI LLMs and LangChain4j.

## Overview

Built with **Spring Boot 3 + LangChain4j + Vue 3**, this platform enables users to generate, preview, and deploy complete web applications through conversational AI. Features real-time streaming responses, persistent chat memory, and one-click deployment.

**Tech Stack:** Spring Boot 3 · LangChain4j · Vue 3 · MyBatis-Flex · PostgreSQL · Redis · Caffeine

---

## Key Features

### Intelligent Code Generation
Natural language to complete web apps (single HTML or multi-file mode) with SSE streaming output and smart regex-based code parsing.

### Live Preview & Deployment
Instant preview of generated apps + one-click deployment to shareable URLs with organized file management.

### Application Management
User authentication, CRUD operations, role-based permissions (AOP), pagination, and featured apps showcase.

### Persistent Chat Memory
PostgreSQL storage + Redis-backed memory for context-aware conversations with cursor-based pagination.

---

## Tech Highlights

**AI Integration**
- OpenAI GPT models with LangChain4j framework
- Reactor-based streaming for real-time responses
- Custom system prompts for domain-specific generation
- Structured output parsing with annotated POJOs

**Data Layer**
- MyBatis-Flex with QueryWrapper pattern
- PostgreSQL with optimized indexing
- Multi-level caching: Caffeine (in-memory) + Redis (distributed)

**Architecture**
- SSE for streaming communication
- Design patterns: Template Method, Strategy, Factory, Facade
- AOP-based authorization with custom annotations
- RESTful APIs with SpringDoc OpenAPI 3

---

## Project Structure

```
ai-code-backend/
├── src/main/java/com/zxuhan/aicodebackend/
│   ├── ai/                    # LangChain4j AI services & factories
│   ├── core/                  # Business logic (Facade, parsers, savers)
│   ├── controller/            # REST API endpoints
|   ├── service/               # Business services
│   ├── model/
│   │   ├── entity/           # Database entities
│   │   ├── enums/            # Enumaration types
│   │   ├── dto/              # Request/Response objects
│   │   └── vo/               # View objects
│   ├── config/               # Spring configurations
│   ├── aop/                  # Authorization interceptors
│   └── exception/            # Global exception handling
└── src/main/resources/
├── prompt/               # LLM system prompts
├── application.yml       # Main configuration
└── create_table.sql      # Database schema
```

## Quick Start

### Prerequisites
- **Java:** JDK 21
- **Database:** PostgreSQL 16+
- **Cache:** Redis 6+
- **Build:** Gradle 8.14+
- **Frontend:** Node.js 18+

### Setup

**1. Database Initialization**
```bash
createdb ai_code_gen
psql -d ai_code_gen -f src/main/resources/create_table.sql
```

**2. Configuration**

Update src/main/resources/application.yml:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ai_code_gen
    username: your_username
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password
```

Create src/main/resources/application-local.yml:
```yaml
langchain4j:
  open-ai:
    chat-model:
      model-name: gpt-4o-mini
      api-key: <Your API key>
      log-requests: true
      log-responses: true
      max-tokens: 8192
      strict-json-schema: true
      response-format: json_object
    streaming-chat-model:
      model-name: gpt-4o-mini
      api-key: <Your API key> 
      max-tokens: 8192
      log-requests: true
      log-responses: true
```

**3. Start Backend**
```bash
# Clone repository
git clone <repository-url>
cd ai-code-backend

# Run with Gradle
./gradlew bootRun
```

**4. Start Frontend**
```bash
cd frontend
npm install
npm run dev
```
**5. Access Application**
- Frontend: http://localhost:5173
- Backend API: http://localhost:8123/api
- API Docs: http://localhost:8123/api/doc.html
