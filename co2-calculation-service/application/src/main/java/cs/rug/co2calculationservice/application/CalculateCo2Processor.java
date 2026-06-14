package cs.rug.co2calculationservice.application;

import cs.rug.co2calculationservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;
import cs.rug.co2calculationservice.api.model.CalculationError;
import cs.rug.co2calculationservice.api.model.CalculationResult;
import cs.rug.co2calculationservice.api.operations.calculateco2.CalculateCo2Operation;
import cs.rug.co2calculationservice.api.operations.calculateco2.CalculateCo2Request;
import cs.rug.co2calculationservice.api.operations.calculateco2.CalculateCo2Response;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class CalculateCo2Processor implements CalculateCo2Operation {

    private static final String EVENT_TYPE = "KEI_CALCULATION_COMPLETED";
    private static final String CONTRACT_VERSION = "1.0";
    private static final String FAILED_STATUS = "FAILED";
    private static final String RESULT_UNIT = "kgCO2e";
    private static final String NOT_IMPLEMENTED_CODE = "CALCULATION_NOT_IMPLEMENTED";

    @Override
    public CalculateCo2Response process(CalculateCo2Request request) {
        KeiCalculationCompletedEvent event = KeiCalculationCompletedEvent
                .builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(EVENT_TYPE)
                .contractVersion(CONTRACT_VERSION)
                .calculationRequestId(request.getCalculationRequestId())
                .observationId(request.getObservationId())
                .sourceEventId(request.getSourceEventId())
                .occurredAt(Instant.now())
                .calculation(request.getCalculation())
                .kei(request.getKei())
                .execution(request.getExecution())
                .result(CalculationResult
                        .builder()
                        .status(FAILED_STATUS)
                        .unit(RESULT_UNIT)
                        .build())
                .resourceBreakdown(List.of())
                .errors(List.of(CalculationError
                        .builder()
                        .code(NOT_IMPLEMENTED_CODE)
                        .message("CO2 calculation is not implemented yet.")
                        .build()))
                .build();

        return CalculateCo2Response
                .builder()
                .eventId(event.getEventId())
                .status(FAILED_STATUS)
                .event(event)
                .build();
    }
}
