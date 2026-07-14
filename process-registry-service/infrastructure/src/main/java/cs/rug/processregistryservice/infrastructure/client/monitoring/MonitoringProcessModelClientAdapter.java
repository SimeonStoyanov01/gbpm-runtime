package cs.rug.processregistryservice.infrastructure.client.monitoring;

import cs.rug.processregistryservice.api.operations.deployprocess.DeployProcessDefinitionResponse;
import cs.rug.processregistryservice.api.model.ElementKeiAnnotations;
import cs.rug.processregistryservice.api.model.KeiAnnotation;
import cs.rug.processregistryservice.application.out.monitoring.MonitoringProcessModelClient;
import cs.rug.processregistryservice.infrastructure.client.monitoring.dto.ProcessModelElement;
import cs.rug.processregistryservice.infrastructure.client.monitoring.dto.ProcessModelKeiAnnotation;
import cs.rug.processregistryservice.infrastructure.client.monitoring.dto.RegisterProcessModelRequest;
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
        monitoringResultsFeignClient.registerProcessModel(RegisterProcessModelRequest
                .builder()
                .deploymentKey(Long.valueOf(deployedProcess.getDeploymentKey()))
                .processDefinitionKey(Long.valueOf(deployedProcess.getProcessDefinitionKey()))
                .bpmnProcessId(deployedProcess.getBpmnProcessId())
                .version(deployedProcess.getVersion())
                .resourceName(deployedProcess.getResourceName())
                .bpmnXml(new String(bpmnXml, StandardCharsets.UTF_8))
                .elements(elementKeiAnnotations
                        .stream()
                        .map(this::toElement)
                        .toList())
                .build());
    }

    private ProcessModelElement toElement(ElementKeiAnnotations elementKeiAnnotations) {
        return ProcessModelElement
                .builder()
                .bpmnElementId(elementKeiAnnotations.getBpmnElementId())
                .name(elementKeiAnnotations.getElementName())
                .type(elementKeiAnnotations.getElementType())
                .keiAnnotations(elementKeiAnnotations
                        .getKeiAnnotations()
                        .stream()
                        .map(this::toKeiAnnotation)
                        .toList())
                .build();
    }

    private ProcessModelKeiAnnotation toKeiAnnotation(KeiAnnotation keiMetadata) {
        return ProcessModelKeiAnnotation
                .builder()
                .id(keiMetadata.getId())
                .unit(keiMetadata.getUnit())
                .targetValue(keiMetadata.getTargetValue())
                .icon(keiMetadata.getIcon())
                .build();
    }
}
