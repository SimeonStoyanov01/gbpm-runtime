package cs.rug.gbpmruntime.processregistry.application.out.monitoring;

import cs.rug.gbpmruntime.processregistry.api.operations.deployprocess.DeployProcessDefinitionResponse;
import cs.rug.gbpmruntime.processregistry.application.model.bpmn4es.ElementKeiAnnotations;

import java.util.List;

public interface MonitoringProcessModelClient {

    void registerProcessModel(
            DeployProcessDefinitionResponse deployedProcess,
            List<ElementKeiAnnotations> elementKeiAnnotations
    );
}
