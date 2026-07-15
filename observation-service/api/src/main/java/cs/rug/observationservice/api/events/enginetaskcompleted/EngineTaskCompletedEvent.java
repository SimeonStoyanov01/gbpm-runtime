package cs.rug.observationservice.api.events.enginetaskcompleted;

import cs.rug.observationservice.api.model.EngineExecutionContext;
import cs.rug.observationservice.api.model.ResourceUsage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EngineTaskCompletedEvent {
    @Valid
    @NotNull
    private EngineExecutionContext execution;

    @NotBlank
    private String workObjectType;

    @Valid
    private List<ResourceUsage> resourceUsages;
}
