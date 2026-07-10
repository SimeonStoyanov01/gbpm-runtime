package cs.rug.gbpmruntime.processregistry.infrastructure.client.monitoring;

import cs.rug.gbpmruntime.processregistry.api.operations.deployprocess.DeployProcessDefinitionResponse;
import cs.rug.gbpmruntime.processregistry.application.model.bpmn4es.ElementKeiAnnotations;
import cs.rug.gbpmruntime.processregistry.application.model.bpmn4es.KeiMetadata;
import cs.rug.gbpmruntime.processregistry.application.out.monitoring.MonitoringProcessModelClient;
import cs.rug.gbpmruntime.processregistry.infrastructure.client.monitoring.dto.ProcessModelElementDto;
import cs.rug.gbpmruntime.processregistry.infrastructure.client.monitoring.dto.ProcessModelKeiAnnotationDto;
import cs.rug.gbpmruntime.processregistry.infrastructure.client.monitoring.dto.RegisterProcessModelRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MonitoringProcessModelClientAdapter implements MonitoringProcessModelClient {

    private final MonitoringResultsFeignClient monitoringResultsFeignClient;

    @Override
    public void registerProcessModel(
            DeployProcessDefinitionResponse deployedProcess,
            byte[] bpmnXml,
            List<ElementKeiAnnotations> elementKeiAnnotations
    ) {
        monitoringResultsFeignClient.registerProcessModel(RegisterProcessModelRequestDto
                .builder()
                .deploymentKey(Long.valueOf(deployedProcess.getDeploymentKey()))
                .processDefinitionKey(Long.valueOf(deployedProcess.getProcessDefinitionKey()))
                .bpmnProcessId(deployedProcess.getBpmnProcessId())
                .version(deployedProcess.getVersion())
                .resourceName(deployedProcess.getResourceName())
                .bpmnXml(new String(bpmnXml, StandardCharsets.UTF_8))
                .elements(elementKeiAnnotations
                        .stream()
                        .map(this::toElementDto)
                        .toList())
                .build());
    }

    private ProcessModelElementDto toElementDto(ElementKeiAnnotations elementKeiAnnotations) {
        return ProcessModelElementDto
                .builder()
                .bpmnElementId(elementKeiAnnotations.getBpmnElementId())
                .name(elementKeiAnnotations.getElementName())
                .type(elementKeiAnnotations.getElementType())
                .keiAnnotations(elementKeiAnnotations
                        .getKeiMetadata()
                        .stream()
                        .map(this::toKeiDto)
                        .toList())
                .build();
    }

    private ProcessModelKeiAnnotationDto toKeiDto(KeiMetadata keiMetadata) {
        return ProcessModelKeiAnnotationDto
                .builder()
                .id(keiMetadata.getId())
                .unit(keiMetadata.getUnit())
                .targetValue(keiMetadata.getTargetValue())
                .icon(keiMetadata.getIcon())
                .build();
    }
}
