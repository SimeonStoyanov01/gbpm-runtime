package cs.rug.monitoringresultsservice.infrastructure.persistence.adapter;

import cs.rug.monitoringresultsservice.api.model.ThresholdViolation;
import cs.rug.monitoringresultsservice.api.operations.findactiveviolations.FindActiveViolationsRequest;
import cs.rug.monitoringresultsservice.application.out.ThresholdViolationStore;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.KeiResultEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.ThresholdViolationEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.mapper.MonitoringResultEntityMapper;
import cs.rug.monitoringresultsservice.infrastructure.persistence.repository.ThresholdViolationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JpaThresholdViolationStore implements ThresholdViolationStore {

    private final ThresholdViolationJpaRepository thresholdViolationRepository;
    private final MonitoringResultEntityMapper monitoringResultMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ThresholdViolation> findActiveViolations(FindActiveViolationsRequest request) {
        return thresholdViolationRepository
                .findAll()
                .stream()
                .filter(violation -> matchesViolationFilter(violation, request))
                .map(monitoringResultMapper::toThresholdViolation)
                .toList();
    }

    private boolean matchesViolationFilter(ThresholdViolationEntity violation, FindActiveViolationsRequest request) {
        KeiResultEntity result = violation.getKeiResult();
        return result != null
                && matches(
                        request.getProcessDefinitionKey(),
                        result.getProcessInstance().getProcessDefinition().getProcessDefinitionKey()
                )
                && matches(
                        request.getBpmnProcessId(),
                        result.getProcessInstance().getProcessDefinition().getBpmnProcessId()
                );
    }

    private boolean matches(Object filterValue, Object recordValue) {
        return filterValue == null || filterValue.equals(recordValue);
    }
}
