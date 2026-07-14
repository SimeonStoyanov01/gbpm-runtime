package cs.rug.monitoringresultsservice.application.out;

import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;
import cs.rug.monitoringresultsservice.api.operations.findmonitoringrecords.FindMonitoringRecordsRequest;
import cs.rug.monitoringresultsservice.api.operations.findactiveviolations.FindActiveViolationsRequest;
import cs.rug.monitoringresultsservice.api.operations.findprocessinstancedetails.FindProcessInstanceDetailsRequest;
import cs.rug.monitoringresultsservice.api.operations.findprocessinstancedetails.FindProcessInstanceDetailsResponse;

import java.util.List;

public interface MonitoringRecordStore {

    MonitoringRecord saveCalculation(MonitoringRecord record);

    MonitoringRecord saveEvaluation(MonitoringRecord record);

    List<MonitoringRecord> findMonitoringRecords(FindMonitoringRecordsRequest request);

    List<MonitoringRecord> findActiveViolations(FindActiveViolationsRequest request);

    FindProcessInstanceDetailsResponse findProcessInstanceDetails(FindProcessInstanceDetailsRequest request);
}
