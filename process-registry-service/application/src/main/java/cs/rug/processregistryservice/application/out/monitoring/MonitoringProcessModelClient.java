package cs.rug.processregistryservice.application.out.monitoring;

import cs.rug.processregistryservice.api.operations.deployprocess.DeployProcessDefinitionResponse;
import cs.rug.processregistryservice.application.model.bpmn4es.ElementKeiAnnotations;

import java.util.List;

public interface MonitoringProcessModelClient {

    void registerProcessModel(
            DeployProcessDefinitionResponse deployedProcess,
            byte[] bpmnXml,
            List<ElementKeiAnnotations> elementKeiAnnotations
    );
}
