package cs.rug.monitoringresultsservice.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(
        name = "kei_result",
        indexes = {
                @Index(name = "idx_kei_result_process_instance", columnList = "process_instance_id"),
                @Index(name = "idx_kei_result_bpmn_element", columnList = "bpmn_element_id"),
                @Index(name = "idx_kei_result_kei_annotation", columnList = "kei_annotation_id"),
                @Index(name = "idx_kei_result_evaluation_status", columnList = "evaluation_status")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_kei_result_calculation_event", columnNames = "calculation_event_id"),
                @UniqueConstraint(name = "uk_kei_result_evaluation_event", columnNames = "evaluation_event_id"),
                @UniqueConstraint(
                        name = "uk_kei_result_execution_annotation",
                        columnNames = {"process_instance_id", "element_instance_key", "kei_annotation_id"}
                )
        }
)
public class KeiResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_instance_id")
    private ProcessInstanceEntity processInstance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bpmn_element_id")
    private BpmnElementEntity bpmnElement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kei_annotation_id")
    private KeiAnnotationEntity keiAnnotation;

    @Column(name = "element_instance_key")
    private Long elementInstanceKey;

    @Column(name = "calculation_event_id", unique = true)
    private String calculationEventId;

    @Column(name = "evaluation_event_id", unique = true)
    private String evaluationEventId;

    @Column(name = "calculation_request_id")
    private String calculationRequestId;

    @Column(name = "observation_id")
    private String observationId;

    @Column(name = "source_event_id")
    private String sourceEventId;

    @Column(name = "calculated_value", precision = 19, scale = 6)
    private BigDecimal calculatedValue;

    @Column(name = "calculated_unit")
    private String calculatedUnit;

    @Column(name = "calculated_at")
    private Instant calculatedAt;

    @Column(name = "target_value", precision = 19, scale = 6)
    private BigDecimal targetValue;

    @Column(name = "difference", precision = 19, scale = 6)
    private BigDecimal difference;

    @Column(name = "evaluation_status")
    private String evaluationStatus;

    @Column(name = "evaluated_at")
    private Instant evaluatedAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
