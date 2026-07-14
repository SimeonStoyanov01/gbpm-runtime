package cs.rug.monitoringresultsservice.application.processors.registerprocessmodel;

import cs.rug.monitoringresultsservice.api.operations.findprocessmodel.FindProcessModelOperation;
import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.ProcessModelElement;
import cs.rug.monitoringresultsservice.application.out.RegisteredProcessModelStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindProcessModelProcessor implements FindProcessModelOperation {

    private final RegisteredProcessModelStore registeredProcessModelStore;

    @Override
    public List<ProcessModelElement> process(Long processDefinitionKey) {
        return registeredProcessModelStore.findProcessModel(processDefinitionKey);
    }
}
