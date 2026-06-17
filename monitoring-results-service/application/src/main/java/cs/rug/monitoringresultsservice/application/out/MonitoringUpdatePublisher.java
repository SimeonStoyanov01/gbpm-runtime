package cs.rug.monitoringresultsservice.application.out;

import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;
import cs.rug.monitoringresultsservice.api.model.ThresholdViolation;

public interface MonitoringUpdatePublisher {

    void publishCalculationUpdate(MonitoringRecord record);

    void publishEvaluationUpdate(MonitoringRecord record);

    void publishViolationUpdate(ThresholdViolation violation);
}
