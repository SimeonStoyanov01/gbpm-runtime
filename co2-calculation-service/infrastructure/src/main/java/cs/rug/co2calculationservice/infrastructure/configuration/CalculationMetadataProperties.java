package cs.rug.co2calculationservice.infrastructure.configuration;

import cs.rug.co2calculationservice.api.model.CalculationMetadata;
import cs.rug.co2calculationservice.application.out.CalculationMetadataProvider;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "runtime.calculation")
public class CalculationMetadataProperties implements CalculationMetadataProvider {
    @NotBlank
    private String calculatorId;

    @NotBlank
    private String calculationMethod;

    @NotBlank
    private String referenceSetId;

    @Override
    public CalculationMetadata calculationMetadata() {
        return CalculationMetadata
                .builder()
                .calculatorId(calculatorId)
                .calculationMethod(calculationMethod)
                .referenceSetId(referenceSetId)
                .build();
    }
}
