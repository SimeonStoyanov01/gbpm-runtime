# CO2 Calculation Service

Calculates task-level carbon-emission KEIs from runtime resource usage. A calculation request identifies the executed BPMN element and supplies the resources used; the service resolves the corresponding reference data and returns the calculated result.

## Calculation

For every resource, the service combines:

- **Resource usage:** the resource name, time used, and time unit observed at runtime.
- **Resource profile:** how much fuel the resource consumes per unit of time.
- **Emission factor:** the amount of CO2 emitted per unit of that fuel.

```text
fuel consumed = time used x fuel consumed per time unit
resource emissions = fuel consumed x emission factor
task emissions = sum of all resource emissions
```

Example reference data and calculation:

```text
Usage:             Welder for 2 hours
Resource profile:  Welder consumes 5 litres of Diesel per hour
Emission factor:   Diesel emits 2.70553 kg per litre
Result:            2 x 5 x 2.70553 = 27.0553 kg
```

## Persistence

PostgreSQL stores resource profiles and emission factors. Calculation results are passed downstream through messaging rather than stored here.

[`bootstrap/src/main/resources/schema.sql`](bootstrap/src/main/resources/schema.sql) creates and seeds the reference tables. With the default local configuration, Spring Boot runs this script whenever the service starts. The local PostgreSQL container also mounts the script as an initialization script, which PostgreSQL runs only when creating a new database volume. The statements are idempotent, so both paths can use the same file.

## Messaging

- Consumes `kei.calculation.requested.carbon-emissions` from `runtime.observation.events`. The request carries the KEI, workflow execution context, work-object type, and observed resource usage.
- Publishes `kei.calculation.completed` to `runtime.calculation.events` so evaluation and monitoring can use the calculated value, calculation metadata, and per-resource breakdown.
- Publishes `kei.calculation.failed` to `runtime.calculation.events` when the request cannot be calculated, including the calculation context and error details.

## Configuration

| Environment variable | Default |
| --- | --- |
| `SERVER_PORT` | `8092` |
| `RABBITMQ_HOST` / `RABBITMQ_PORT` | `localhost` / `5672` |
| `RABBITMQ_USERNAME` / `RABBITMQ_PASSWORD` | `runtime` / `runtime` |
| `CO2_CALCULATION_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/co2_calculation` |
| `CO2_CALCULATION_DATASOURCE_USERNAME` / `CO2_CALCULATION_DATASOURCE_PASSWORD` | `co2_calculation` / `co2_calculation` |
| `CO2_CALCULATION_CALCULATOR_ID` | `example_co2_calculator` |
| `CO2_CALCULATION_METHOD` | `popescu_resource_co2` |
| `CO2_CALCULATION_REFERENCE_SET_ID` | `popescu-demo-v1` |

Messaging destinations can also be overridden through the properties listed in `bootstrap/src/main/resources/application.yml`.

## Run Locally

RabbitMQ and the `co2_calculation` PostgreSQL database must be available using the defaults in `bootstrap/src/main/resources/application.yml`, or equivalent environment variables.

```bash
mvn clean install
mvn -f bootstrap/pom.xml spring-boot:run
```

## Docker

From this directory:

```bash
docker build -t co2-calculation-service .
```

See the [main README](../README.md) for instructions to deploy the complete system.
