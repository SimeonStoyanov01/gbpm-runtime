package cs.rug.monitoringresultsservice.api.model;

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
public class ThresholdViolation {
    private String eventId;
    private Long processDefinitionKey;
    private String bpmnProcessId;
    private String serviceTaskId;
    private Long processInstanceKey;
    private String emissionType;
    private BigDecimal calculatedValue;
    private BigDecimal targetValue;
    private BigDecimal difference;
    private String status;
    private Instant occurredAt;
}
