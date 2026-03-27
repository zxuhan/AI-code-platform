# ========================================
# ai-code — Backend Dockerfile
# ========================================

# ============ Build stage ============
FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

# 1. Copy pom.xml and prefetch dependencies (Docker layer cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. Copy sources and build the JAR
COPY src ./src
RUN mvn clean package -DskipTests -B

# ============ Runtime stage ============
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# curl: healthcheck.
# chromium + chromium-chromedriver: WebScreenshotUtils (Selenium).
# nss / freetype / harfbuzz / ttf-freefont: required by chromium for any rendering.
RUN apk add --no-cache \
      curl \
      chromium \
      chromium-chromedriver \
      nss \
      freetype \
      harfbuzz \
      ttf-freefont

COPY --from=build /app/target/*.jar app.jar

# Run as non-root
RUN addgroup -g 1000 appuser && \
    adduser -D -u 1000 -G appuser appuser && \
    chown -R appuser:appuser /app
USER appuser

EXPOSE 8123

# WebScreenshotUtils picks these up so it doesn't try to download a
# glibc-linked chromedriver into Alpine.
ENV CHROME_BIN=/usr/bin/chromium-browser
ENV CHROMEDRIVER_PATH=/usr/bin/chromedriver

# JVM tuning (override via JAVA_OPTS in compose / .env)
ENV JAVA_OPTS="-Xms256m -Xmx768m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=3 \
    CMD curl -f http://localhost:8123/api/health/ || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --spring.profiles.active=prod"]
