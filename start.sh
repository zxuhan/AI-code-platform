#!/bin/bash

# ========================================
# ai-code - One-click start script
# ========================================

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Logging helpers
info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

error() {
    echo -e "${RED}[ERROR]${NC} $1"
    exit 1
}

# Check whether Docker is installed
check_docker() {
    if ! command -v docker &> /dev/null; then
        error "Docker is not installed. Please install Docker first."
    fi

    # Prefer "docker compose" (v2); fall back to "docker-compose" (v1)
    if docker compose version &> /dev/null; then
        DOCKER_COMPOSE="docker compose"
    elif command -v docker-compose &> /dev/null; then
        DOCKER_COMPOSE="docker-compose"
    else
        error "Docker Compose is not installed. Please install Docker Compose first."
    fi

    info "Docker and Docker Compose are installed ✓"
}

# Check the .env file
check_env() {
    if [ ! -f .env ]; then
        warn ".env file not found; creating from .env.example..."
        cp .env.example .env
        warn "Please edit .env and fill in the required API key:"
        warn "  - GEMINI_API_KEY  (https://aistudio.google.com/app/apikey)"
        echo ""
        read -p "Edit the .env file now? (y/n) " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            ${EDITOR:-vi} .env
        else
            error "Please edit .env, then re-run this script."
        fi
    fi

    # Verify required environment variables
    set -a; source .env; set +a

    if [ -z "$GEMINI_API_KEY" ] || [ "$GEMINI_API_KEY" = "your-gemini-api-key" ]; then
        error "Please set GEMINI_API_KEY in the .env file"
    fi

    info "Environment variables look good ✓"
}

# Stop and clean up existing containers
cleanup() {
    info "Stopping and cleaning up existing containers..."
    $DOCKER_COMPOSE down
}

# Build and start services
start() {
    info "Building and starting services..."
    info "First build downloads ~500 MB of Maven deps — please wait."
    echo ""

    $DOCKER_COMPOSE up -d --build

    echo ""
    info "Services started successfully!"
}

# Wait for services to become ready
wait_for_services() {
    info "Waiting for services to start..."
    echo ""

    info "Waiting for MySQL..."
    timeout=60
    counter=0
    until $DOCKER_COMPOSE exec -T mysql mysqladmin ping -h localhost -u root -p"${MYSQL_ROOT_PASSWORD:-123456}" --silent &> /dev/null; do
        sleep 2
        counter=$((counter + 2))
        if [ $counter -ge $timeout ]; then
            error "MySQL startup timed out"
        fi
        echo -n "."
    done
    echo ""
    info "MySQL is ready ✓"

    info "Waiting for Redis..."
    counter=0
    until $DOCKER_COMPOSE exec -T redis redis-cli ping &> /dev/null; do
        sleep 1
        counter=$((counter + 1))
        if [ $counter -ge 30 ]; then
            error "Redis startup timed out"
        fi
        echo -n "."
    done
    echo ""
    info "Redis is ready ✓"

    info "Waiting for backend service..."
    counter=0
    until curl -s http://127.0.0.1:${BACKEND_PORT:-8124}/api/health/ > /dev/null 2>&1; do
        sleep 3
        counter=$((counter + 3))
        if [ $counter -ge 180 ]; then
            error "Backend startup timed out"
        fi
        echo -n "."
    done
    echo ""
    info "Backend is ready ✓"

    info "Waiting for frontend service..."
    counter=0
    until curl -s http://127.0.0.1:${FRONTEND_PORT:-8082}/health > /dev/null 2>&1; do
        sleep 2
        counter=$((counter + 2))
        if [ $counter -ge 60 ]; then
            error "Frontend startup timed out"
        fi
        echo -n "."
    done
    echo ""
    info "Frontend is ready ✓"
}

# Show access info
show_info() {
    echo ""
    echo "========================================="
    echo "  ai-code is up and running!"
    echo "========================================="
    echo ""
    echo "📱 URLs:"
    echo "  Frontend:    http://127.0.0.1:${FRONTEND_PORT:-8082}"
    echo "  Backend API: http://127.0.0.1:${BACKEND_PORT:-8124}/api"
    echo "  API docs:    http://127.0.0.1:${BACKEND_PORT:-8124}/api/doc.html"
    echo ""
    echo "📊 Check service status:"
    echo "  docker compose ps"
    echo ""
    echo "📋 View service logs:"
    echo "  docker compose logs -f [service]"
    echo "  services: backend, frontend, mysql, redis"
    echo ""
    echo "🛑 Stop services:"
    echo "  docker compose down"
    echo ""
    echo "========================================="
}

# Main flow
main() {
    echo ""
    echo "========================================="
    echo "  ai-code - One-click start"
    echo "========================================="
    echo ""

    check_docker
    check_env
    cleanup
    start
    wait_for_services
    show_info
}

# Run
main
