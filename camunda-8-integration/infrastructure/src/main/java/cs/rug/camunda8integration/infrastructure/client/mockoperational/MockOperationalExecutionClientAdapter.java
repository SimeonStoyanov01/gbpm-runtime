package cs.rug.camunda8integration.infrastructure.client.mockoperational;

import cs.rug.camunda8integration.api.events.enginetaskcompleted.ResourceUsageFact;
import cs.rug.camunda8integration.application.model.OperationalExecutionRequest;
import cs.rug.camunda8integration.application.model.OperationalExecutionResult;
import cs.rug.camunda8integration.application.out.OperationalExecutionClient;
import cs.rug.camunda8integration.infrastructure.client.mockoperational.dto.CreateExecutionRunRequestDto;
import cs.rug.camunda8integration.infrastructure.client.mockoperational.dto.CreateExecutionRunResponseDto;
import cs.rug.camunda8integration.infrastructure.client.mockoperational.dto.ResourceUsageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MockOperationalExecutionClientAdapter implements OperationalExecutionClient {

    private final MockOperationalServiceClient mockOperationalServiceClient;

    @Override
    public OperationalExecutionResult execute(OperationalExecutionRequest request) {
        CreateExecutionRunResponseDto response = mockOperationalServiceClient.createExecutionRun(
                CreateExecutionRunRequestDto
                        .builder()
                        .orderId(request.getOrderId())
                        .bpmnElementId(request.getBpmnElementId())
                        .jobType(request.getJobType())
                        .workObject(request.getWorkObject())
                        .assignedResources(request.getAssignedResources())
                        .build()
        );

        return OperationalExecutionResult
                .builder()
                .runId(response.getRunId())
                .status(response.getStatus())
                .resourceUsages(toResourceUsageFacts(response.getResourceUsages()))
                .build();
    }

    private List<ResourceUsageFact> toResourceUsageFacts(List<ResourceUsageDto> resourceUsages) {
        if (resourceUsages == null) {
            return List.of();
        }

        return resourceUsages
                .stream()
                .map(resourceUsage -> ResourceUsageFact
                        .builder()
                        .resourceName(resourceUsage.getResourceName())
                        .timeUsed(resourceUsage.getTimeUsed())
                        .unit(resourceUsage.getUnit())
                        .build())
                .toList();
    }
}
