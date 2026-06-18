package cs.rug.monitoringresultsservice.api.operations.findprocessinstancedetails;

import cs.rug.monitoringresultsservice.api.base.ProcessorResponse;
import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;
import cs.rug.monitoringresultsservice.api.model.ThresholdViolation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindProcessInstanceDetailsResponse implements ProcessorResponse {
    private Long processDefinitionKey;
    private String bpmnProcessId;
    private Long processInstanceKey;
    private String bpmnXml;
    private List<MonitoringRecord> records;
    private List<ThresholdViolation> violations;
}
