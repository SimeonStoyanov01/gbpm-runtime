# KEI Evaluation Service

Evaluates calculated KEI values against numeric targets declared in BPMN KEI annotations. A value at or below its target is classified as `WITHIN_TARGET`; a higher value is classified as `VIOLATED`. The evaluation also records the difference between the calculated and target values.

## Messaging

- Consumes `kei.calculation.completed` from `runtime.calculation.events`.
- Publishes `kei.evaluation.completed` to `runtime.evaluation.events` so monitoring consumers can persist and expose the result. Calculations without a numeric target do not produce an evaluation event.

## Configuration

| Environment variable | Default |
| --- | --- |
| `SERVER_PORT` | `8093` |
| `RABBITMQ_HOST` / `RABBITMQ_PORT` | `localhost` / `5672` |
| `RABBITMQ_USERNAME` / `RABBITMQ_PASSWORD` | `runtime` / `runtime` |


## Run Locally

RabbitMQ must be available using the defaults above, or equivalent environment variables.

```bash
mvn clean install
mvn -f bootstrap/pom.xml spring-boot:run
```

## Docker

From this directory:

```bash
docker build -t kei-evaluation-service .
```

See the [main README](../README.md) for instructions to deploy the complete system.
