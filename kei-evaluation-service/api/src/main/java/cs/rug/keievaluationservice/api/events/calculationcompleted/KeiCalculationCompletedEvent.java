package cs.rug.keievaluationservice.api.events.calculationcompleted;

import cs.rug.keievaluationservice.api.model.CalculationDescriptor;
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

    @NotBlank
    private String eventType;

    @NotBlank

    @NotNull
    private Instant occurredAt;

    @Valid
    @NotNull
    private CalculationDescriptor calculation;

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
    private List<ResourceBreakdown> resourceBreakdown;
}
