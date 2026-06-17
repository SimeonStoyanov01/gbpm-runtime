package cs.rug.monitoringresultsservice.application.out;

import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;
import cs.rug.monitoringresultsservice.api.operations.findmonitoringrecords.FindMonitoringRecordsRequest;
import cs.rug.monitoringresultsservice.api.operations.recordcalculation.RecordCalculationRequest;
import cs.rug.monitoringresultsservice.api.operations.recordevaluation.RecordEvaluationRequest;
import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.RegisterProcessModelRequest;
import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.RegisterProcessModelResponse;

import java.util.List;

public interface MonitoringRecordStore {

    MonitoringRecord saveCalculation(RecordCalculationRequest request);

    MonitoringRecord saveEvaluation(RecordEvaluationRequest request);

    List<MonitoringRecord> findMonitoringRecords(FindMonitoringRecordsRequest request);

    RegisterProcessModelResponse registerProcessModel(RegisterProcessModelRequest request);
}
