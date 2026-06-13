package cs.rug.camunda8integration.infrastructure.client.mockoperational.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateExecutionRunResponseDto {
    private String runId;
    private String status;
    private List<ResourceUsageDto> resourceUsages;
}
