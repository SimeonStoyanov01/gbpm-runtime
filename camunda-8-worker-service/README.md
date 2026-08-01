# Camunda 8 Worker Service

Executes Camunda service-task jobs with job type `default-worker`. For each activated job it requests mock operational execution data, publishes the completed task and its resource usage for observation, and then completes the Camunda job.

The worker expects an optional `orderId` process variable and a `workObject` object containing `objectId`, `type`, and `material`.

## Messaging

- Publishes `engine.task.completed` to `runtime.camunda8.events`. The event carries Camunda execution identifiers, work-object type, and the resource usage returned by Mock Operational Service.

## Configuration

| Environment variable | Default |
| --- | --- |
| `CAMUNDA_REST_ADDRESS` | `http://localhost:8088` |
| `CAMUNDA_GRPC_ADDRESS` | `http://localhost:26500` |
| `MOCK_OPERATIONAL_SERVICE_URL` | `http://localhost:8091` |
| `RABBITMQ_HOST` / `RABBITMQ_PORT` | `localhost` / `5672` |
| `RABBITMQ_USERNAME` / `RABBITMQ_PASSWORD` | `runtime` / `runtime` |
| `CAMUNDA8_WORKER_ORDER_ID_VARIABLE` | `orderId` |
| `CAMUNDA8_WORKER_WORK_OBJECT_VARIABLE` | `workObject` |

Messaging destinations and additional worker values can be overridden through the properties listed in `src/main/resources/application.yml`.

## Run Locally

Camunda 8, RabbitMQ, and Mock Operational Service must be available using the defaults above, or equivalent environment variables.

```bash
mvn clean install
mvn spring-boot:run
```

## Docker

From this directory:

```bash
docker build -t camunda-8-worker-service .
```

See the [main README](../README.md) for instructions to deploy the complete system.
