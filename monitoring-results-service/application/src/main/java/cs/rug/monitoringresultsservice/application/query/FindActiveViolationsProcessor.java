package cs.rug.monitoringresultsservice.application.query;

import cs.rug.monitoringresultsservice.api.operations.findactiveviolations.FindActiveViolationsOperation;
import cs.rug.monitoringresultsservice.api.operations.findactiveviolations.FindActiveViolationsRequest;
import cs.rug.monitoringresultsservice.api.operations.findactiveviolations.FindActiveViolationsResponse;
import cs.rug.monitoringresultsservice.application.out.ThresholdViolationStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindActiveViolationsProcessor implements FindActiveViolationsOperation {

    private final ThresholdViolationStore thresholdViolationStore;

    @Override
    public FindActiveViolationsResponse process(FindActiveViolationsRequest request) {
        return FindActiveViolationsResponse
                .builder()
                .violations(thresholdViolationStore.findActiveViolations(request))
                .build();
    }
}
