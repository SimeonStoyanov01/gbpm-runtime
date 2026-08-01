package cs.rug.observationservice.infrastructure.client.processregistry;

import cs.rug.observationservice.infrastructure.client.processregistry.dto.FindActivityKeiAnnotationsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "processRegistryClient",
        url = "${process-registry.url}",
        path = "/api/process-definitions"
)
public interface ProcessRegistryFeignClient {

    @GetMapping("/{processDefinitionKey}/activities/{bpmnElementId}/keis")
    FindActivityKeiAnnotationsResponse findActivityKeiAnnotations(
            @PathVariable Long processDefinitionKey,
            @PathVariable String bpmnElementId
    );
}
