#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
NETWORK_NAME="runtime-local"

COMPOSE_FILES=(
  "${SCRIPT_DIR}/runtime/docker-compose.yml"
  "${SCRIPT_DIR}/camunda8/docker-compose.yml"
  "${SCRIPT_DIR}/infra/docker-compose.yml"
)

CONTAINERS=(
  process-registry-service
  mock-operational-service
  observation-service
  co2-calculation-service
  kei-evaluation-service
  monitoring-results-service
  camunda-8-integration
  camunda-8-worker-service
  orchestration
  elasticsearch
  runtime-rabbitmq
  co2-calculation-postgres
  monitoring-results-postgres
)

VOLUMES=(
  runtime-infra-local_rabbitmq-data
  runtime-infra-local_co2-calculation-postgres-data
  runtime-infra-local_monitoring-results-postgres-data
  runtime-camunda8-local_zeebe
  runtime-camunda8-local_elastic
)

echo "Stopping local Runtime Compose projects..."
for compose_file in "${COMPOSE_FILES[@]}"; do
  docker compose -f "${compose_file}" down --volumes --remove-orphans --rmi local || true
done

# Remove containers left behind by older Compose project names.
for container in "${CONTAINERS[@]}"; do
  if docker container inspect "${container}" >/dev/null 2>&1; then
    docker container rm --force "${container}" >/dev/null
  fi
done

for volume in "${VOLUMES[@]}"; do
  if docker volume inspect "${volume}" >/dev/null 2>&1; then
    docker volume rm "${volume}" >/dev/null
  fi
done

if docker network inspect "${NETWORK_NAME}" >/dev/null 2>&1; then
  docker network rm "${NETWORK_NAME}" >/dev/null
fi

echo "Starting a clean local Runtime stack..."
exec "${SCRIPT_DIR}/start-local.sh"
