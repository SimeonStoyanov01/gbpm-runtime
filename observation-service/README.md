# Observation Service

Turns completed workflow-engine tasks into calculator requests. It looks up the KEI annotations registered for the executed BPMN element and publishes one calculation request for each applicable KEI.

## Messaging

- Consumes `engine.task.completed` from `runtime.camunda8.events`. The event carries workflow execution context, work-object type, and observed resource usage.
- Publishes `kei.calculation.requested.<keiId>` to `runtime.observation.events`. The request combines the execution facts with the KEI metadata retrieved from Process Registry.

## Configuration

| Environment variable | Default |
| --- | --- |
| `PROCESS_REGISTRY_URL` | `http://localhost:8080` |
| `RABBITMQ_HOST` / `RABBITMQ_PORT` | `localhost` / `5672` |
| `RABBITMQ_USERNAME` / `RABBITMQ_PASSWORD` | `runtime` / `runtime` |

Messaging destinations can also be overridden through the properties listed in `bootstrap/src/main/resources/application.yml`.

## Run Locally

RabbitMQ and Process Registry must be available using the defaults above, or equivalent environment variables.

```bash
mvn clean install
mvn -f bootstrap/pom.xml spring-boot:run
```

## Docker

From this directory:

```bash
docker build -t observation-service .
```

See the [main README](../README.md) for instructions to deploy the complete system.
