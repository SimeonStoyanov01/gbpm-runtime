package cs.rug.monitoringresultsservice.api.events.evaluationcompleted;

import cs.rug.monitoringresultsservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;
import cs.rug.monitoringresultsservice.api.model.EvaluationDetails;
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
public class KeiEvaluationCompletedEvent {
    @NotBlank
    private String eventId;

    @NotNull
    private Instant occurredAt;

    @Valid
    @NotNull
    private KeiCalculationCompletedEvent calculationEvent;

    @Valid
    @NotNull
    private EvaluationDetails evaluation;
}
