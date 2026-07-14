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

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "kei_resource_breakdown",
        indexes = @Index(name = "idx_kei_resource_breakdown_result", columnList = "kei_result_id")
)
public class KeiResourceBreakdownEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kei_result_id", nullable = false)
    private KeiResultEntity keiResult;

    @Column(name = "resource_name", nullable = false)
    private String resourceName;

    @Column(name = "emission_value", nullable = false, precision = 19, scale = 6)
    private BigDecimal emissionValue;

    @Column(name = "unit", nullable = false)
    private String unit;
}
