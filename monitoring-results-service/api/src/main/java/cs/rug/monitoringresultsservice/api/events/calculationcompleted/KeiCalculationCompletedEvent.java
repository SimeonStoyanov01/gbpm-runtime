package cs.rug.monitoringresultsservice.api.events.calculationcompleted;

import cs.rug.monitoringresultsservice.api.model.CalculationMetadata;
import cs.rug.monitoringresultsservice.api.model.CalculationResult;
import cs.rug.monitoringresultsservice.api.model.EngineExecutionContext;
import cs.rug.monitoringresultsservice.api.model.KeiAnnotation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

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
}
