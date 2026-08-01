package cs.rug.monitoringresultsservice.application.processors.monitoringrecord.record;

import cs.rug.monitoringresultsservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;
import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;
import cs.rug.monitoringresultsservice.api.operations.recordcalculation.RecordCalculationOperation;
import cs.rug.monitoringresultsservice.application.mapper.CalculationCompletedEventMapper;
import cs.rug.monitoringresultsservice.application.out.MonitoringRecordStore;
import cs.rug.monitoringresultsservice.application.out.MonitoringUpdatePublisher;
import cs.rug.monitoringresultsservice.application.validation.EventContractValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordCalculationProcessor implements RecordCalculationOperation {

    private final MonitoringRecordStore monitoringRecordStore;
    private final MonitoringUpdatePublisher monitoringUpdatePublisher;
    private final CalculationCompletedEventMapper calculationCompletedEventMapper;
    private final EventContractValidator eventContractValidator;

    @Override
    public void process(KeiCalculationCompletedEvent event) {
        List<String> validationErrors = eventContractValidator.validate(event);
        if (!validationErrors.isEmpty()) {
            log.warn(
                    "Skipping invalid KEI calculation completed event: {}",
                    String.join("; ", validationErrors)
            );
            return;
        }

        log.info("Received KEI calculation completed event: eventId={}", event.getEventId());
        if (hasTargetValue(event)) {
            log.info(
                    "Skipping calculation monitoring projection because target value is present: eventId={}, keiId={}",
                    event.getEventId(),
                    event.getKei().getId()
            );
            return;
        }

        MonitoringRecord savedRecord = monitoringRecordStore.saveCalculation(
                calculationCompletedEventMapper.toMonitoringRecord(event)
        );
        monitoringUpdatePublisher.publishCalculationUpdate(savedRecord);
    }

    private boolean hasTargetValue(KeiCalculationCompletedEvent event) {
        return event.getKei().getTargetValue() != null && !event.getKei().getTargetValue().isBlank();
    }
}
