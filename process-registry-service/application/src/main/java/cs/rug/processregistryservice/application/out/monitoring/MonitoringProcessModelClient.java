package cs.rug.processregistryservice.application.out.monitoring;

import cs.rug.processregistryservice.api.operations.deployprocess.DeployProcessDefinitionResponse;
import cs.rug.processregistryservice.api.model.ElementKeiAnnotations;

import java.util.List;

public interface MonitoringProcessModelClient {

    void registerProcessModel(
            DeployProcessDefinitionResponse deployedProcess,
            byte[] bpmnXml,
            List<ElementKeiAnnotations> elementKeiAnnotations
    );

    List<ElementKeiAnnotations> findProcessModel(Long processDefinitionKey);
}
