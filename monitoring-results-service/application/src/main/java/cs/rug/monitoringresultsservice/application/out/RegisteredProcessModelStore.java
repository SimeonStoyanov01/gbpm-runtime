package cs.rug.monitoringresultsservice.application.out;

import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.RegisterProcessModelRequest;

public interface RegisteredProcessModelStore {

    void registerProcessModel(RegisterProcessModelRequest request);
}
