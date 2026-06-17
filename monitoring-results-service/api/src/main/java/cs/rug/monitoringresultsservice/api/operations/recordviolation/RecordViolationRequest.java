package cs.rug.monitoringresultsservice.api.operations.recordviolation;

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
public class RecordViolationRequest implements ProcessorRequest {
    private String eventId;
    private Instant occurredAt;
    private Long processDefinitionKey;
    private String bpmnProcessId;
    private String serviceTaskId;
    private Long processInstanceKey;
    private String emissionType;
    private BigDecimal calculatedValue;
    private BigDecimal targetValue;
    private BigDecimal difference;
    private String status;
}
