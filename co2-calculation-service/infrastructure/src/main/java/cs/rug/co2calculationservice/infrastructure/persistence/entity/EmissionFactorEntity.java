package cs.rug.co2calculationservice.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "emission_factors")
public class EmissionFactorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String referenceSetId;

    @Column(nullable = false)
    private String fuelType;

    @Column(nullable = false)
    private String unit;

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal factor;
}
