package cs.rug.keievaluationservice.api.events.calculationcompleted;

import cs.rug.keievaluationservice.api.model.CalculationMetadata;
import cs.rug.keievaluationservice.api.model.CalculationResult;
import cs.rug.keievaluationservice.api.model.EngineExecutionContext;
import cs.rug.keievaluationservice.api.model.KeiAnnotation;
import cs.rug.keievaluationservice.api.model.ResourceBreakdown;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeiCalculationCompletedEvent {
    @NotBlank
    private String eventId;

    @NotNull
    private Instant occurredAt;

    @Valid
    @NotNull
    private CalculationMetadata calculation;

    @Valid
    @NotNull
    private KeiAnnotation kei;

    @Valid
    @NotNull
    private EngineExecutionContext execution;

    @Valid
    @NotNull
    private CalculationResult result;

    @Valid
    @NotNull
    private List<ResourceBreakdown> resourceBreakdown;
}
