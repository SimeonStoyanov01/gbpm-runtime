package cs.rug.monitoringresultsservice.application.out;

import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;

public interface MonitoringUpdatePublisher {

    void publishCalculationUpdate(MonitoringRecord record);

    void publishEvaluationUpdate(MonitoringRecord record);

    void publishViolationUpdate(MonitoringRecord record);
}
