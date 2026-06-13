package cs.rug.camunda8integration.application.out;

import cs.rug.camunda8integration.application.model.OperationalExecutionRequest;
import cs.rug.camunda8integration.application.model.OperationalExecutionResult;

public interface OperationalExecutionClient {

    OperationalExecutionResult execute(OperationalExecutionRequest request);
}
