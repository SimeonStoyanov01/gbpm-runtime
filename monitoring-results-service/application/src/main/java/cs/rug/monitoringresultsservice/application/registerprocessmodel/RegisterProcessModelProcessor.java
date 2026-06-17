package cs.rug.monitoringresultsservice.application.registerprocessmodel;

import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.RegisterProcessModelOperation;
import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.RegisterProcessModelRequest;
import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.RegisterProcessModelResponse;
import cs.rug.monitoringresultsservice.application.out.MonitoringRecordStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterProcessModelProcessor implements RegisterProcessModelOperation {

    private final MonitoringRecordStore monitoringRecordStore;

    @Override
    public RegisterProcessModelResponse process(RegisterProcessModelRequest request) {
        return monitoringRecordStore.registerProcessModel(request);
    }
}
