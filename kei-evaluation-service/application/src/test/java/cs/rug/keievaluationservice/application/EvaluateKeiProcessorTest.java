package cs.rug.keievaluationservice.application;

import cs.rug.keievaluationservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;
import cs.rug.keievaluationservice.api.events.evaluationcompleted.KeiEvaluationCompletedEvent;
import cs.rug.keievaluationservice.api.events.thresholdviolationdetected.ThresholdViolationDetectedEvent;
import cs.rug.keievaluationservice.api.model.CalculationDescriptor;
import cs.rug.keievaluationservice.api.model.CalculationResult;
import cs.rug.keievaluationservice.api.model.EngineExecutionContext;
import cs.rug.keievaluationservice.api.model.KeiAnnotation;
import cs.rug.keievaluationservice.api.operations.evaluatekei.EvaluateKeiRequest;
import cs.rug.keievaluationservice.api.operations.evaluatekei.EvaluateKeiResponse;
import cs.rug.keievaluationservice.application.out.EvaluationEventPublisher;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class EvaluateKeiProcessorTest {

    private static final Validator VALIDATOR = Validation
            .buildDefaultValidatorFactory()
            .getValidator();

    @Test
    void publishesEvaluationAndViolationWhenTargetIsExceeded() {
        CapturingEvaluationEventPublisher publisher = new CapturingEvaluationEventPublisher();
        EvaluateKeiProcessor processor = new EvaluateKeiProcessor(
                publisher,
                VALIDATOR
        );

        EvaluateKeiResponse response = processor.process(EvaluateKeiRequest
                .builder()
                .event(calculationCompletedEvent("108.2212", "8"))
                .build());

        assertThat(response.getEvaluated()).isTrue();
        assertThat(publisher.evaluationEvent.getEvaluation().getStatus()).isEqualTo("VIOLATED");
        assertThat(publisher.violationEvent).isNotNull();
        assertThat(publisher.violationEvent.getProcessDefinitionKey()).isEqualTo(2251799813698152L);
        assertThat(publisher.violationEvent.getBpmnProcessId()).isEqualTo("Process_1vxlsro");
        assertThat(publisher.violationEvent.getServiceTaskId()).isEqualTo("Activity_04tru70");
        assertThat(publisher.violationEvent.getProcessInstanceKey()).isEqualTo(2251799813698625L);
        assertThat(publisher.violationEvent.getEmissionType()).isEqualTo("carbon-emissions");
        assertThat(publisher.violationEvent.getCalculatedValue()).isEqualByComparingTo("108.2212");
        assertThat(publisher.violationEvent.getTargetValue()).isEqualByComparingTo("8");
        assertThat(publisher.violationEvent.getDifference()).isEqualByComparingTo("100.2212");
        assertThat(publisher.violationEvent.getStatus()).isEqualTo("VIOLATED");
    }

    @Test
    void publishesOnlyEvaluationWhenWithinTarget() {
        CapturingEvaluationEventPublisher publisher = new CapturingEvaluationEventPublisher();
        EvaluateKeiProcessor processor = new EvaluateKeiProcessor(
                publisher,
                VALIDATOR
        );

        EvaluateKeiResponse response = processor.process(EvaluateKeiRequest
                .builder()
                .event(calculationCompletedEvent("7.5", "8"))
                .build());

        assertThat(response.getEvaluated()).isTrue();
        assertThat(publisher.evaluationEvent.getEvaluation().getStatus()).isEqualTo("WITHIN_TARGET");
        assertThat(publisher.violationEvent).isNull();
    }

    @Test
    void publishesNothingWhenTargetIsMissing() {
        CapturingEvaluationEventPublisher publisher = new CapturingEvaluationEventPublisher();
        EvaluateKeiProcessor processor = new EvaluateKeiProcessor(
                publisher,
                VALIDATOR
        );

        EvaluateKeiResponse response = processor.process(EvaluateKeiRequest
                .builder()
                .event(calculationCompletedEvent("7.5", null))
                .build());

        assertThat(response.getEvaluated()).isFalse();
        assertThat(publisher.evaluationEvent).isNull();
        assertThat(publisher.violationEvent).isNull();
    }

    private KeiCalculationCompletedEvent calculationCompletedEvent(String value, String targetValue) {
        return KeiCalculationCompletedEvent
                .builder()
                .eventId("calculation-event-1")
                .eventType("KEI_CALCULATION_COMPLETED")
                .contractVersion("1.0")
                .calculationRequestId("calculation-request-1")
                .observationId("observation-1")
                .sourceEventId("engine-event-1")
                .occurredAt(Instant.parse("2026-06-13T14:55:00Z"))
                .calculation(CalculationDescriptor
                        .builder()
                        .strategy("POPESCU_RESOURCE_CO2")
                        .referenceSetId("popescu-demo-v1")
                        .build())
                .kei(KeiAnnotation
                        .builder()
                        .id("carbon-emissions")
                        .unit("kg")
                        .targetValue(targetValue)
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
                .result(CalculationResult
                        .builder()
                        .status("SUCCEEDED")
                        .value(new BigDecimal(value))
                        .unit("kg")
                        .build())
                .build();
    }

    private static class CapturingEvaluationEventPublisher implements EvaluationEventPublisher {
        private KeiEvaluationCompletedEvent evaluationEvent;
        private ThresholdViolationDetectedEvent violationEvent;

        @Override
        public void publishEvaluation(KeiEvaluationCompletedEvent event) {
            this.evaluationEvent = event;
        }

        @Override
        public void publishViolation(ThresholdViolationDetectedEvent event) {
            this.violationEvent = event;
        }
    }
}
