# Camunda 8 Integration

Provides the HTTP adapter between Runtime services and a Camunda 8 orchestration cluster. It deploys BPMN resources, starts process instances, finds active user tasks, and completes user tasks with optional process variables.

## API

| Method | Path | Purpose |
| --- | --- | --- |
| `POST` | `/api/v1/camunda8/process-definitions/deploy` | Deploy a BPMN resource to Camunda 8. |
| `POST` | `/api/v1/camunda8/process-definitions/instances` | Start a process instance by process-definition key. |
| `GET` | `/api/v1/camunda8/process-instances/{processInstanceKey}/user-tasks` | Return active user tasks for a process instance. |
| `POST` | `/api/v1/camunda8/user-tasks/{userTaskKey}/completion` | Complete a user task. |

## Configuration

| Environment variable | Default |
| --- | --- |
| `SERVER_PORT` | `8090` |
| `CAMUNDA_REST_ADDRESS` | `http://localhost:8088` |
| `CAMUNDA_GRPC_ADDRESS` | `http://localhost:26500` |

## Run Locally

A Camunda 8 orchestration cluster must be available using the addresses above, or equivalent environment variables.

```bash
mvn clean install
mvn -f bootstrap/pom.xml spring-boot:run
```

## Docker

From this directory:

```bash
docker build -t camunda-8-integration .
```

See the [main README](../README.md) for instructions to deploy the complete system.
