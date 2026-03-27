# ========================================
# ai-code — Frontend Dockerfile
# ========================================

# ============ Build stage ============
FROM node:22-alpine AS build

WORKDIR /app

# 1. Install deps (Docker layer cache)
COPY package*.json ./
RUN npm ci && npm cache clean --force

# 2. Build for production
COPY . .
RUN npm run build

# ============ Runtime stage ============
FROM nginx:alpine

# curl: healthcheck
RUN apk add --no-cache curl

# Static assets
COPY --from=build /app/dist /usr/share/nginx/html

# Server config
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Non-root nginx — needs writable cache/log/pid paths
RUN mkdir -p /var/cache/nginx /var/log/nginx /tmp/nginx && \
    chown -R nginx:nginx /var/cache/nginx /var/log/nginx /tmp/nginx /usr/share/nginx/html && \
    sed -i 's|/run/nginx.pid|/tmp/nginx/nginx.pid|g' /etc/nginx/nginx.conf && \
    touch /tmp/nginx/nginx.pid && chown nginx:nginx /tmp/nginx/nginx.pid

USER nginx

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
    CMD curl -f http://localhost/ || exit 1

CMD ["nginx", "-g", "daemon off;"]
