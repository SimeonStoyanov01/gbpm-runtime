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
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(
        name = "threshold_violation",
        indexes = {
                @Index(name = "idx_threshold_violation_event", columnList = "event_id"),
                @Index(name = "idx_threshold_violation_result", columnList = "kei_result_id"),
                @Index(name = "idx_threshold_violation_status", columnList = "status")
        }
)
public class ThresholdViolationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", unique = true)
    private String eventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kei_result_id")
    private KeiResultEntity keiResult;

    @Column(name = "status")
    private String status;

    @Column(name = "calculated_value", precision = 19, scale = 6)
    private BigDecimal calculatedValue;

    @Column(name = "target_value", precision = 19, scale = 6)
    private BigDecimal targetValue;

    @Column(name = "difference", precision = 19, scale = 6)
    private BigDecimal difference;

    @Column(name = "occurred_at")
    private Instant occurredAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
