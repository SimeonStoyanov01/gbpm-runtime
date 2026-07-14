package cs.rug.mockoperationalservice.application;

import cs.rug.mockoperationalservice.api.exceptions.ExecutionRunNotFoundException;
import cs.rug.mockoperationalservice.api.model.ResourceUsage;
import cs.rug.mockoperationalservice.api.operations.createexecutionrun.CreateExecutionRunOperation;
import cs.rug.mockoperationalservice.api.operations.createexecutionrun.CreateExecutionRunRequest;
import cs.rug.mockoperationalservice.api.operations.createexecutionrun.CreateExecutionRunResponse;
import cs.rug.mockoperationalservice.application.model.MockExecutionRunRecord;
import cs.rug.mockoperationalservice.application.out.ExecutionRunLookup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateExecutionRunProcessor implements CreateExecutionRunOperation {

    private static final String COMPLETED_STATUS = "COMPLETED";

    private final ExecutionRunLookup executionRunLookup;

    @Override
    public CreateExecutionRunResponse process(CreateExecutionRunRequest request) {
        MockExecutionRunRecord record = executionRunLookup
                .findExecutionRun(
                        request.getBpmnElementId(),
                        request.getWorkObject().getType(),
                        request.getWorkObject().getMaterial()
                )
                .orElseThrow(() -> new ExecutionRunNotFoundException(
                        "No mock execution run found for BPMN element " + request.getBpmnElementId()
                                + " and work object: " + request.getWorkObject().getType()
                                + "/" + request.getWorkObject().getMaterial()
                ));

        return CreateExecutionRunResponse
                .builder()
                .runId("RUN-" + UUID.randomUUID())
                .status(COMPLETED_STATUS)
                .resourceUsages(List.of(ResourceUsage
                        .builder()
                        .resourceName(record.getResourceName())
                        .timeUsed(record.getTimeUsed())
                        .unit(record.getUnit())
                        .build()))
                .build();
    }
}
