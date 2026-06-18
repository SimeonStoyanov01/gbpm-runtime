package cs.rug.monitoringresultsservice.api.operations.findprocessinstancedetails;

import cs.rug.monitoringresultsservice.api.base.ProcessorRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindProcessInstanceDetailsRequest implements ProcessorRequest {
    private Long processInstanceKey;
}
