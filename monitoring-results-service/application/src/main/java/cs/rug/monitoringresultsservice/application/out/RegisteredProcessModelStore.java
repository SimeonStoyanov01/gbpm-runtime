package cs.rug.monitoringresultsservice.application.out;

import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.RegisterProcessModelRequest;
import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.RegisterProcessModelResponse;

public interface RegisteredProcessModelStore {

    RegisterProcessModelResponse registerProcessModel(RegisterProcessModelRequest request);
}
