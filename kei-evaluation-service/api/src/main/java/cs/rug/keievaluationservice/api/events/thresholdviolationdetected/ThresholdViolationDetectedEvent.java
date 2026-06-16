package cs.rug.keievaluationservice.api.events.thresholdviolationdetected;

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
public class ThresholdViolationDetectedEvent {
    private String eventId;
    private String eventType;
    private String contractVersion;
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
