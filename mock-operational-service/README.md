# Mock Operational Service

Provides deterministic operational execution data for the prototype. It matches the BPMN element, work-object type, and material against [`mock-execution-runs.json`](infrastructure/src/main/resources/mock-execution-runs.json), then returns the resource usage associated with that task execution.

## API

| Method | Path | Purpose |
| --- | --- | --- |
| `POST` | `/api/execution-runs` | Resolve and return a completed mock execution run for the supplied order, BPMN element, and work object. |

The response contains a generated run ID, execution status, and the resources used with their usage time and unit.

## Configuration

| Environment variable | Default |
| --- | --- |
| `SERVER_PORT` | `8091` |

## Run Locally

```bash
mvn clean install
mvn -f bootstrap/pom.xml spring-boot:run
```

## Docker

From this directory:

```bash
docker build -t mock-operational-service .
```

See the [main README](../README.md) for instructions to deploy the complete system.
