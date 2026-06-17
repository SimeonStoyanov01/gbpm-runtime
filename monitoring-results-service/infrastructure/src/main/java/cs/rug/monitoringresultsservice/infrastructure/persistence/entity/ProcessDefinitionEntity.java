package cs.rug.monitoringresultsservice.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(
        name = "process_definition",
        indexes = {
                @Index(name = "idx_process_definition_key", columnList = "process_definition_key"),
                @Index(name = "idx_process_definition_bpmn_process", columnList = "bpmn_process_id")
        }
)
public class ProcessDefinitionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "process_definition_key", unique = true)
    private Long processDefinitionKey;

    @Column(name = "bpmn_process_id")
    private String bpmnProcessId;

    @Column(name = "deployment_key")
    private Long deploymentKey;

    @Column(name = "version")
    private Integer version;

    @Column(name = "deployed_at")
    private Instant deployedAt;
}
