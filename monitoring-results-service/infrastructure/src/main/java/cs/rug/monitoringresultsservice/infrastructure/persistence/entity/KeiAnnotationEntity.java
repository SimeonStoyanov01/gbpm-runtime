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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "kei_annotation",
        indexes = @Index(name = "idx_kei_annotation_element", columnList = "bpmn_element_id"),
        uniqueConstraints = @UniqueConstraint(
                name = "uk_kei_annotation_element_kei",
                columnNames = {"bpmn_element_id", "kei_id"}
        )
)
public class KeiAnnotationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bpmn_element_id")
    private BpmnElementEntity bpmnElement;

    @Column(name = "kei_id")
    private String keiId;

    @Column(name = "unit")
    private String unit;

    @Column(name = "target_value")
    private String targetValue;

    @Column(name = "icon")
    private String icon;
}
