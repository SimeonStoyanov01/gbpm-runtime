package cs.rug.monitoringresultsservice.application.processors.monitoringrecord.record;

import cs.rug.monitoringresultsservice.api.events.evaluationcompleted.KeiEvaluationCompletedEvent;
import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;
import cs.rug.monitoringresultsservice.api.operations.recordevaluation.RecordEvaluationOperation;
import cs.rug.monitoringresultsservice.application.mapper.EvaluationCompletedEventMapper;
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
public class RecordEvaluationProcessor implements RecordEvaluationOperation {

    private static final String EVALUATION_STATUS_VIOLATED = "VIOLATED";

    private final MonitoringRecordStore monitoringRecordStore;
    private final MonitoringUpdatePublisher monitoringUpdatePublisher;
    private final EvaluationCompletedEventMapper evaluationCompletedEventMapper;
    private final EventContractValidator eventContractValidator;

    @Override
    public void process(KeiEvaluationCompletedEvent event) {
        List<String> validationErrors = eventContractValidator.validate(event);
        if (!validationErrors.isEmpty()) {
            log.warn(
                    "Skipping invalid KEI evaluation completed event: {}",
                    String.join("; ", validationErrors)
            );
            return;
        }

        log.info("Received KEI evaluation completed event: eventId={}", event.getEventId());
        MonitoringRecord savedRecord = monitoringRecordStore.saveEvaluation(
                evaluationCompletedEventMapper.toMonitoringRecord(event)
        );

        monitoringUpdatePublisher.publishEvaluationUpdate(savedRecord);
        if (EVALUATION_STATUS_VIOLATED.equals(savedRecord.getEvaluationStatus())) {
            monitoringUpdatePublisher.publishViolationUpdate(savedRecord);
        }
    }
}
