package cs.rug.monitoringresultsservice.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringRecord {
    private String calculationEventId;
    private String evaluationEventId;
    private String engineType;
    private String calculatorId;
    private String calculationMethod;
    private String referenceSetId;
    private Long processDefinitionKey;
    private String bpmnProcessId;
    private Long processInstanceKey;
    private Long elementInstanceKey;
    private String bpmnElementId;
    private String elementName;
    private String workObjectType;
    private String keiId;
    private BigDecimal calculatedValue;
    private String calculatedUnit;
    private BigDecimal targetValue;
    private BigDecimal difference;
    private String evaluationStatus;
    private Instant calculatedAt;
    private Instant evaluatedAt;
    private List<ResourceBreakdown> resourceBreakdown;
}
