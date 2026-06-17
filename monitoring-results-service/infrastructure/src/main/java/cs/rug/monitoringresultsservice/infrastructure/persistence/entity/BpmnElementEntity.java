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

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "bpmn_element",
        indexes = @Index(name = "idx_bpmn_element_definition", columnList = "process_definition_id"),
        uniqueConstraints = @UniqueConstraint(
                name = "uk_bpmn_element_definition_element",
                columnNames = {"process_definition_id", "bpmn_element_id"}
        )
)
public class BpmnElementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_definition_id")
    private ProcessDefinitionEntity processDefinition;

    @Column(name = "bpmn_element_id")
    private String bpmnElementId;

    @Column(name = "element_name")
    private String elementName;

    @Column(name = "element_type")
    private String elementType;
}
