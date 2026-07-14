package cs.rug.monitoringresultsservice.api.operations.findmonitoringrecords;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindMonitoringRecordsRequest {
    private Long processInstanceKey;
    private Long processDefinitionKey;
    private String bpmnProcessId;
    private String evaluationStatus;
}
