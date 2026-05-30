# Local Camunda 8

This Docker Compose setup is for local development only.

## Start

```bash
cd deploy/local/camunda8
docker compose up -d
```

## Stop

```bash
docker compose down
```

## Remove Volumes

```bash
docker compose down -v
```

## Local Endpoints

- Camunda orchestration REST/UI: http://localhost:8088
- Camunda gRPC: http://localhost:26500
- Camunda management health: http://localhost:9600/actuator/health/status
- Elasticsearch: http://localhost:9200
