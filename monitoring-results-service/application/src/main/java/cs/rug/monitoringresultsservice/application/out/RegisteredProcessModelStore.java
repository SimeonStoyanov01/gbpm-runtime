package cs.rug.monitoringresultsservice.application.out;

import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.ProcessModelElement;
import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.RegisterProcessModelRequest;

import java.util.List;

public interface RegisteredProcessModelStore {

    void registerProcessModel(RegisterProcessModelRequest request);

    List<ProcessModelElement> findProcessModel(Long processDefinitionKey);
}
