package cs.rug.monitoringresultsservice.api.operations.findprocessinstancedetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindProcessInstanceDetailsRequest {
    private Long processInstanceKey;
}
