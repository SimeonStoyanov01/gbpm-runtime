package cs.rug.co2calculationservice.application;

import cs.rug.co2calculationservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;
import cs.rug.co2calculationservice.api.events.calculationfailed.KeiCalculationFailedEvent;
import cs.rug.co2calculationservice.api.model.CalculationDescriptor;
import cs.rug.co2calculationservice.api.model.CalculationInputs;
import cs.rug.co2calculationservice.api.model.EngineExecutionContext;
import cs.rug.co2calculationservice.api.model.KeiAnnotation;
import cs.rug.co2calculationservice.api.model.ResourceUsage;
import cs.rug.co2calculationservice.api.operations.calculateco2.CalculateCo2Request;
import cs.rug.co2calculationservice.api.operations.calculateco2.CalculateCo2Response;
import cs.rug.co2calculationservice.application.model.EmissionFactor;
import cs.rug.co2calculationservice.application.model.ResourceProfile;
import cs.rug.co2calculationservice.application.factory.CalculationCompletedEventFactory;
import cs.rug.co2calculationservice.application.factory.CalculationFailedEventFactory;
import cs.rug.co2calculationservice.application.out.CalculationResultPublisher;
import cs.rug.co2calculationservice.application.out.EmissionFactorLookup;
import cs.rug.co2calculationservice.application.out.ResourceProfileLookup;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CalculateCo2ProcessorTest {

    private static final Validator VALIDATOR = Validation
            .buildDefaultValidatorFactory()
            .getValidator();

    @Test
    void calculatesPopescuResourceCo2ForWelderExample() {
        CapturingPublisher publisher = new CapturingPublisher();
        CalculateCo2Processor processor = new CalculateCo2Processor(
                new StaticResourceProfileLookup(),
                new StaticEmissionFactorLookup(),
                publisher,
                VALIDATOR,
                new CalculationCompletedEventFactory(),
                new CalculationFailedEventFactory()
        );

        CalculateCo2Response response = processor.process(CalculateCo2Request
                .builder()
                .calculationRequestId("calculation-request-1")
                .observationId("observation-1")
                .sourceEventId("source-event-1")
                .calculation(CalculationDescriptor
                        .builder()
                        .strategy("POPESCU_RESOURCE_CO2")
                        .referenceSetId("popescu-demo-v1")
                        .build())
                .kei(KeiAnnotation
                        .builder()
                        .id("carbon-emissions")
                        .unit("kg")
                        .targetValue("8")
                        .build())
                .execution(EngineExecutionContext
                        .builder()
                        .engineType("CAMUNDA_8")
                        .processDefinitionKey(2251799813698152L)
                        .bpmnProcessId("Process_1vxlsro")
                        .processInstanceKey(2251799813698625L)
                        .elementInstanceKey(2251799813698632L)
                        .bpmnElementId("Activity_04tru70")
                        .build())
                .inputs(CalculationInputs
                        .builder()
                        .resourceUsages(List.of(ResourceUsage
                                .builder()
                                .resourceName("Welder")
                                .timeUsed(8.0)
                                .unit("hour")
                                .build()))
                        .build())
                .build());

        assertThat(response.getStatus()).isEqualTo("SUCCEEDED");
        assertThat(response.getEvent().getResult().getValue()).isEqualByComparingTo("108.2212");
        assertThat(response.getEvent().getResult().getUnit()).isEqualTo("kg");
        assertThat(response.getEvent().getResourceBreakdown()).hasSize(1);
        assertThat(response.getEvent().getResourceBreakdown().getFirst().getEmissionValue())
                .isEqualByComparingTo("108.2212");
        assertThat(response.getEvent().getCalculationRequestId()).isEqualTo("calculation-request-1");
        assertThat(response.getEvent().getObservationId()).isEqualTo("observation-1");
        assertThat(response.getEvent().getSourceEventId()).isEqualTo("source-event-1");
        assertThat(response.getEvent()).isSameAs(publisher.publishedCompletedEvent);
    }

    @Test
    void publishesFailedEventForInvalidRequest() {
        CapturingPublisher publisher = new CapturingPublisher();
        CalculateCo2Processor processor = new CalculateCo2Processor(
                new StaticResourceProfileLookup(),
                new StaticEmissionFactorLookup(),
                publisher,
                VALIDATOR,
                new CalculationCompletedEventFactory(),
                new CalculationFailedEventFactory()
        );

        CalculateCo2Response response = processor.process(CalculateCo2Request
                .builder()
                .calculationRequestId("calculation-request-1")
                .observationId("observation-1")
                .sourceEventId("source-event-1")
                .calculation(CalculationDescriptor
                        .builder()
                        .strategy("POPESCU_RESOURCE_CO2")
                        .referenceSetId("popescu-demo-v1")
                        .build())
                .kei(KeiAnnotation
                        .builder()
                        .id("carbon-emissions")
                        .unit("kg")
                        .build())
                .execution(EngineExecutionContext
                        .builder()
                        .engineType("CAMUNDA_8")
                        .processDefinitionKey(2251799813698152L)
                        .bpmnProcessId("Process_1vxlsro")
                        .processInstanceKey(2251799813698625L)
                        .elementInstanceKey(2251799813698632L)
                        .bpmnElementId("Activity_04tru70")
                        .build())
                .inputs(CalculationInputs
                        .builder()
                        .resourceUsages(List.of(ResourceUsage
                                .builder()
                                .resourceName("Welder")
                                .unit("hour")
                                .build()))
                        .build())
                .build());

        assertThat(response.getStatus()).isEqualTo("FAILED");
        assertThat(response.getEvent()).isNull();
        assertThat(response.getEventId()).isEqualTo(publisher.publishedFailedEvent.getEventId());
        assertThat(publisher.publishedCompletedEvent).isNull();
        assertThat(publisher.publishedFailedEvent.getError())
                .satisfies(error -> {
                    assertThat(error.getCode()).isEqualTo("INVALID_CALCULATION_REQUEST");
                    assertThat(error.getMessage()).contains("timeUsed");
                });
    }

    @Test
    void publishesFailedEventWhenResourceUsageUnitDoesNotMatchProfileTimeUnit() {
        CapturingPublisher publisher = new CapturingPublisher();
        CalculateCo2Processor processor = new CalculateCo2Processor(
                new StaticResourceProfileLookup(),
                new StaticEmissionFactorLookup(),
                publisher,
                VALIDATOR,
                new CalculationCompletedEventFactory(),
                new CalculationFailedEventFactory()
        );

        CalculateCo2Response response = processor.process(CalculateCo2Request
                .builder()
                .calculationRequestId("calculation-request-1")
                .observationId("observation-1")
                .sourceEventId("source-event-1")
                .calculation(CalculationDescriptor
                        .builder()
                        .strategy("POPESCU_RESOURCE_CO2")
                        .referenceSetId("popescu-demo-v1")
                        .build())
                .kei(KeiAnnotation
                        .builder()
                        .id("carbon-emissions")
                        .unit("kg")
                        .build())
                .execution(EngineExecutionContext
                        .builder()
                        .engineType("CAMUNDA_8")
                        .processDefinitionKey(2251799813698152L)
                        .bpmnProcessId("Process_1vxlsro")
                        .processInstanceKey(2251799813698625L)
                        .elementInstanceKey(2251799813698632L)
                        .bpmnElementId("Activity_04tru70")
                        .build())
                .inputs(CalculationInputs
                        .builder()
                        .resourceUsages(List.of(ResourceUsage
                                .builder()
                                .resourceName("Welder")
                                .timeUsed(8.0)
                                .unit("minute")
                                .build()))
                        .build())
                .build());

        assertThat(response.getStatus()).isEqualTo("FAILED");
        assertThat(response.getEvent()).isNull();
        assertThat(response.getEventId()).isEqualTo(publisher.publishedFailedEvent.getEventId());
        assertThat(publisher.publishedCompletedEvent).isNull();
        assertThat(publisher.publishedFailedEvent.getError())
                .satisfies(error -> assertThat(error.getCode()).isEqualTo("RESOURCE_USAGE_UNIT_MISMATCH"));
    }

    private static class StaticResourceProfileLookup implements ResourceProfileLookup {

        @Override
        public Optional<ResourceProfile> findResourceProfile(String referenceSetId, String resourceName) {
            if (!"popescu-demo-v1".equals(referenceSetId) || !"Welder".equals(resourceName)) {
                return Optional.empty();
            }

            return Optional.of(ResourceProfile
                    .builder()
                    .name("Welder")
                    .type("atomic")
                    .fuelPerUse(new BigDecimal("5.0"))
                    .fuelType("Diesel")
                    .fuelUnit("l")
                    .timeUnit("hour")
                    .build());
        }
    }

    private static class StaticEmissionFactorLookup implements EmissionFactorLookup {

        @Override
        public Optional<EmissionFactor> findEmissionFactor(String referenceSetId, String fuelType) {
            if (!"popescu-demo-v1".equals(referenceSetId) || !"Diesel".equals(fuelType)) {
                return Optional.empty();
            }

            return Optional.of(EmissionFactor
                    .builder()
                    .fuelType("Diesel")
                    .unit("l")
                    .factor(new BigDecimal("2.70553"))
                    .build());
        }
    }

    private static class CapturingPublisher implements CalculationResultPublisher {
        private KeiCalculationCompletedEvent publishedCompletedEvent;
        private KeiCalculationFailedEvent publishedFailedEvent;

        @Override
        public void publishCompleted(KeiCalculationCompletedEvent event) {
            this.publishedCompletedEvent = event;
        }

        @Override
        public void publishFailed(KeiCalculationFailedEvent event) {
            this.publishedFailedEvent = event;
        }
    }
}
