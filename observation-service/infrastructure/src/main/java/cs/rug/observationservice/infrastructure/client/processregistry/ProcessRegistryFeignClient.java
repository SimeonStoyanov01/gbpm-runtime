package cs.rug.observationservice.infrastructure.client.processregistry;

import cs.rug.observationservice.infrastructure.client.processregistry.dto.FindActivityKeiAnnotationsResponseDto;
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
    FindActivityKeiAnnotationsResponseDto findActivityKeiAnnotations(
            @PathVariable Long processDefinitionKey,
            @PathVariable String bpmnElementId
    );
}
