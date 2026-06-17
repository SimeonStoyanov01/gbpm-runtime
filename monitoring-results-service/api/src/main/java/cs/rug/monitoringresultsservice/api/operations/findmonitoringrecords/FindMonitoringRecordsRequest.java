package cs.rug.monitoringresultsservice.api.operations.findmonitoringrecords;

import cs.rug.monitoringresultsservice.api.base.ProcessorRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindMonitoringRecordsRequest implements ProcessorRequest {
    private Long processInstanceKey;
    private Long processDefinitionKey;
    private String bpmnProcessId;
    private String evaluationStatus;
}
