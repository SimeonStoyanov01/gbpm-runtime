#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
NETWORK_NAME="runtime-local"

if ! docker network inspect "${NETWORK_NAME}" >/dev/null 2>&1; then
  docker network create "${NETWORK_NAME}" >/dev/null
fi

docker compose -f "${SCRIPT_DIR}/infra/docker-compose.yml" up -d --wait
docker compose -f "${SCRIPT_DIR}/camunda8/docker-compose.yml" up -d --wait
docker compose -f "${SCRIPT_DIR}/runtime/docker-compose.yml" up -d
