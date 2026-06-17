package cs.rug.mockoperationalservice.application.out;

import cs.rug.mockoperationalservice.application.model.MockExecutionRunRecord;

import java.util.Optional;

public interface ExecutionRunLookup {

    Optional<MockExecutionRunRecord> findExecutionRun(
            String bpmnElementId,
            String objectType,
            String material
    );
}
