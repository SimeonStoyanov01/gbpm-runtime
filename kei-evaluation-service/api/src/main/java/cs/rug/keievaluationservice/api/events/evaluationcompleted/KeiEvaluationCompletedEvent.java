package cs.rug.keievaluationservice.api.events.evaluationcompleted;

import cs.rug.keievaluationservice.api.model.CalculatedResult;
import cs.rug.keievaluationservice.api.model.CalculationDescriptor;
import cs.rug.keievaluationservice.api.model.EngineExecutionContext;
import cs.rug.keievaluationservice.api.model.EvaluationDetails;
import cs.rug.keievaluationservice.api.model.KeiAnnotation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeiEvaluationCompletedEvent {
    private String eventId;
    private String eventType;
    private String contractVersion;
    private String calculationResultId;
    private String calculationRequestId;
    private String observationId;
    private String sourceEventId;
    private Instant occurredAt;
    private CalculationDescriptor calculation;
    private KeiAnnotation kei;
    private EngineExecutionContext execution;
    private CalculatedResult calculatedResult;
    private EvaluationDetails evaluation;
}
