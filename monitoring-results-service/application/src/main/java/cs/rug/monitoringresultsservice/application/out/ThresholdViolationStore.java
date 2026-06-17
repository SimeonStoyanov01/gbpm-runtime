package cs.rug.monitoringresultsservice.application.out;

import cs.rug.monitoringresultsservice.api.model.ThresholdViolation;
import cs.rug.monitoringresultsservice.api.operations.findactiveviolations.FindActiveViolationsRequest;

import java.util.List;

public interface ThresholdViolationStore {

    ThresholdViolation saveOrUpdateActiveViolation(ThresholdViolation violation);

    List<ThresholdViolation> findActiveViolations(FindActiveViolationsRequest request);
}
