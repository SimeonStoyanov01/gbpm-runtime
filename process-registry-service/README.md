# Process Registry Service

Provides the entry point for deploying BPMN models and starting process instances. During deployment it sends the model to the workflow-engine integration, extracts BPMN4ES KEI annotations, and registers the process model and annotations with the Monitoring Results Service.

## API

| Method | Path | Purpose |
| --- | --- | --- |
| `POST` | `/api/process-definitions/deploy` | Deploy a multipart BPMN resource and register its KEI metadata. |
| `POST` | `/api/process-definitions/{processDefinitionKey}/instances` | Start a process instance with the JSON request body as process variables. |
| `GET` | `/api/process-definitions/{processDefinitionKey}/keis` | Return all KEI annotations for a process definition. |
| `GET` | `/api/process-definitions/{processDefinitionKey}/activities/{bpmnElementId}/keis` | Return KEIs associated with one BPMN element. |

## Configuration

| Environment variable | Default |
| --- | --- |
| `PROCESS_REGISTRY_PORT` | `8080` |
| `CAMUNDA8_INTEGRATION_URL` | `http://localhost:8090` |
| `MONITORING_RESULTS_SERVICE_URL` | `http://localhost:8094` |

## Run Locally

Camunda 8 Integration and Monitoring Results must be available using the URLs above, or equivalent environment variables.

```bash
mvn clean install
mvn -f bootstrap/pom.xml spring-boot:run
```

## Docker

From this directory:

```bash
docker build -t process-registry-service .
```

See the [main README](../README.md) for instructions to deploy the complete system.
