package cs.rug.gbpmruntime.processregistry.infrastructure.clients.dto.startprocess;

import cs.rug.gbpmruntime.common.api.base.ProcessorResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartProcessInEngineResponse implements ProcessorResponse {
    private String processDefinitionKey;
    private String bpmnProcessId;
    private Integer version;
    private String processInstanceKey;
    private String tenantId;
}
