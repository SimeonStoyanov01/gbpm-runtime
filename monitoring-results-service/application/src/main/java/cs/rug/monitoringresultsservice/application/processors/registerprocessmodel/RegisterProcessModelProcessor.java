package cs.rug.monitoringresultsservice.application.processors.registerprocessmodel;

import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.RegisterProcessModelOperation;
import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.RegisterProcessModelRequest;
import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.RegisterProcessModelResponse;
import cs.rug.monitoringresultsservice.application.out.RegisteredProcessModelStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterProcessModelProcessor implements RegisterProcessModelOperation {

    private final RegisteredProcessModelStore registeredProcessModelStore;

    @Override
    public RegisterProcessModelResponse process(RegisterProcessModelRequest request) {
        return registeredProcessModelStore.registerProcessModel(request);
    }
}
