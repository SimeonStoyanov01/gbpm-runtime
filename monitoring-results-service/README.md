# Monitoring Results Service

Stores deployed process metadata and runtime KEI results. Calculation and evaluation events are projected into queryable monitoring records and sent to connected frontend clients through STOMP/WebSocket updates.

## API

| Method | Path | Purpose |
| --- | --- | --- |
| `GET` | `/api/monitoring/records` | Query records by process instance, process definition, BPMN process ID, or evaluation status. |
| `GET` | `/api/monitoring/process-instances/{processInstanceKey}` | Return one process instance, its BPMN model, records, and violations. |
| `GET` | `/api/monitoring/violations/active` | Query persisted violated evaluations. |
| `POST` | `/api/monitoring/process-models` | Register a deployed BPMN model and its KEI metadata. |
| `GET` | `/api/monitoring/process-models/{processDefinitionKey}/elements` | Return registered BPMN elements and KEI annotations. |

The STOMP endpoint is `/ws/monitoring`. Updates are published to `/topic/monitoring/calculations`, `/topic/monitoring/evaluations`, and `/topic/monitoring/violations`.

## Persistence

PostgreSQL stores process definitions, BPMN elements, KEI annotations, process instances, calculated/evaluated KEI results, and per-resource calculation breakdowns.

The schema is created and updated by Hibernate when the service starts.

## Messaging

- Consumes `kei.calculation.completed` from `runtime.calculation.events` to store calculations that do not require evaluation.
- Consumes `kei.evaluation.completed` from `runtime.evaluation.events` to store evaluated results and expose violated evaluations.

## Configuration

| Environment variable | Default |
| --- | --- |
| `MONITORING_RESULTS_SERVICE_PORT` | `8094` |
| `RABBITMQ_HOST` / `RABBITMQ_PORT` | `localhost` / `5672` |
| `RABBITMQ_USERNAME` / `RABBITMQ_PASSWORD` | `runtime` / `runtime` |
| `MONITORING_RESULTS_DATASOURCE_URL` | `jdbc:postgresql://localhost:5434/monitoring_results` |
| `MONITORING_RESULTS_DATASOURCE_USERNAME` / `MONITORING_RESULTS_DATASOURCE_PASSWORD` | `monitoring_results` / `monitoring_results` |

Messaging destinations can also be overridden through the properties listed in `bootstrap/src/main/resources/application.yml`.

## Run Locally

RabbitMQ and the `monitoring_results` PostgreSQL database must be available using the defaults above, or equivalent environment variables.

```bash
mvn clean install
mvn -f bootstrap/pom.xml spring-boot:run
```

## Docker

From this directory:

```bash
docker build -t monitoring-results-service .
```

See the [main README](../README.md) for instructions to deploy the complete system.
