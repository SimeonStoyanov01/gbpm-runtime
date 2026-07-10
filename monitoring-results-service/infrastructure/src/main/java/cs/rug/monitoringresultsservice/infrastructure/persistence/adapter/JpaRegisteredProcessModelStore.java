package cs.rug.monitoringresultsservice.infrastructure.persistence.adapter;

import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.ProcessModelElement;
import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.ProcessModelKeiAnnotation;
import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.RegisterProcessModelRequest;
import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.RegisterProcessModelResponse;
import cs.rug.monitoringresultsservice.application.out.RegisteredProcessModelStore;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.BpmnElementEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.KeiAnnotationEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.entity.ProcessDefinitionEntity;
import cs.rug.monitoringresultsservice.infrastructure.persistence.repository.BpmnElementJpaRepository;
import cs.rug.monitoringresultsservice.infrastructure.persistence.repository.KeiAnnotationJpaRepository;
import cs.rug.monitoringresultsservice.infrastructure.persistence.repository.ProcessDefinitionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JpaRegisteredProcessModelStore implements RegisteredProcessModelStore {

    private final ProcessDefinitionJpaRepository processDefinitionRepository;
    private final BpmnElementJpaRepository bpmnElementRepository;
    private final KeiAnnotationJpaRepository keiAnnotationRepository;

    @Override
    @Transactional
    public RegisterProcessModelResponse registerProcessModel(RegisterProcessModelRequest request) {
        ProcessDefinitionEntity processDefinition = saveProcessDefinition(request);

        int annotationCount = 0;
        if (request.getElements() != null) {
            for (ProcessModelElement element : request.getElements()) {
                BpmnElementEntity bpmnElement = saveBpmnElement(processDefinition, element);

                if (element.getKeiAnnotations() != null) {
                    for (ProcessModelKeiAnnotation annotation : element.getKeiAnnotations()) {
                        saveKeiAnnotation(bpmnElement, annotation);
                        annotationCount++;
                    }
                }
            }
        }

        return RegisterProcessModelResponse
                .builder()
                .processDefinitionKey(request.getProcessDefinitionKey())
                .elementCount(request.getElements() == null ? 0 : request.getElements().size())
                .keiAnnotationCount(annotationCount)
                .build();
    }

    private ProcessDefinitionEntity saveProcessDefinition(RegisterProcessModelRequest request) {
        processDefinitionRepository.saveRegisteredProcessDefinition(
                UUID.randomUUID(),
                request.getProcessDefinitionKey(),
                request.getBpmnProcessId(),
                request.getDeploymentKey(),
                request.getVersion(),
                request.getBpmnXml()
        );

        return processDefinitionRepository
                .findByProcessDefinitionKey(request.getProcessDefinitionKey())
                .orElseThrow(() -> new IllegalStateException(
                        "Registered process definition save did not return a row: "
                                + request.getProcessDefinitionKey()
                ));
    }

    private BpmnElementEntity saveBpmnElement(
            ProcessDefinitionEntity processDefinition,
            ProcessModelElement element
    ) {
        BpmnElementEntity bpmnElement = addBpmnElementIfMissing(processDefinition, element.getBpmnElementId());
        bpmnElement.setElementName(element.getName());
        bpmnElement.setElementType(element.getType());
        return bpmnElementRepository.save(bpmnElement);
    }

    private KeiAnnotationEntity saveKeiAnnotation(
            BpmnElementEntity bpmnElement,
            ProcessModelKeiAnnotation annotation
    ) {
        KeiAnnotationEntity entity = addKeiAnnotationIfMissing(bpmnElement, annotation.getId());
        entity.setUnit(annotation.getUnit());
        entity.setTargetValue(annotation.getTargetValue());
        entity.setIcon(annotation.getIcon());
        return keiAnnotationRepository.save(entity);
    }

    private BpmnElementEntity addBpmnElementIfMissing(
            ProcessDefinitionEntity processDefinition,
            String bpmnElementId
    ) {
        bpmnElementRepository.insertIfMissing(UUID.randomUUID(), processDefinition.getId(), bpmnElementId);
        return bpmnElementRepository
                .findByProcessDefinitionAndBpmnElementId(processDefinition, bpmnElementId)
                .orElseThrow(() -> new IllegalStateException(
                        "BPMN element add did not return a row: " + bpmnElementId
                ));
    }

    private KeiAnnotationEntity addKeiAnnotationIfMissing(BpmnElementEntity bpmnElement, String keiId) {
        keiAnnotationRepository.insertIfMissing(UUID.randomUUID(), bpmnElement.getId(), keiId);
        return keiAnnotationRepository
                .findByBpmnElementAndKeiId(bpmnElement, keiId)
                .orElseThrow(() -> new IllegalStateException(
                        "KEI annotation add did not return a row: " + keiId
                ));
    }
}
