package cs.rug.monitoringresultsservice.api.operations.findactiveviolations;

import cs.rug.monitoringresultsservice.api.base.Processor;
import cs.rug.monitoringresultsservice.api.model.MonitoringRecord;

import java.util.List;

public interface FindActiveViolationsOperation
        extends Processor<FindActiveViolationsRequest, List<MonitoringRecord>> {
}
