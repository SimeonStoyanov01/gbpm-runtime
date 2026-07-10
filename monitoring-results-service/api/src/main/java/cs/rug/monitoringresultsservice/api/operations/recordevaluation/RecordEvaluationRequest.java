package cs.rug.monitoringresultsservice.api.operations.recordevaluation;

import cs.rug.monitoringresultsservice.api.base.ProcessorRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordEvaluationRequest implements ProcessorRequest {
    private String eventId;
    private String calculationResultId;
    private Instant occurredAt;
    private Long processDefinitionKey;
    private String bpmnProcessId;
    private Long processInstanceKey;
    private Long elementInstanceKey;
    private String bpmnElementId;
    private String keiId;
    private BigDecimal calculatedValue;
    private String calculatedUnit;
    private Instant calculatedAt;
    private BigDecimal targetValue;
    private BigDecimal difference;
    private String evaluationStatus;
}
