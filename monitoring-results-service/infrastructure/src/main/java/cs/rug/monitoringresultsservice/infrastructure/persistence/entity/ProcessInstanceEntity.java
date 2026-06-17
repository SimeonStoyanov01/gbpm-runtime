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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "process_instance",
        indexes = {
                @Index(name = "idx_process_instance_key", columnList = "process_instance_key"),
                @Index(name = "idx_process_instance_definition", columnList = "process_definition_id")
        }
)
public class ProcessInstanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "process_instance_key", unique = true)
    private Long processInstanceKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_definition_id")
    private ProcessDefinitionEntity processDefinition;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "ended_at")
    private Instant endedAt;
}
