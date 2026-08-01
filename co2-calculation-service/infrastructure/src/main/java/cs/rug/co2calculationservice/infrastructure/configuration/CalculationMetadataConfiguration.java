package cs.rug.co2calculationservice.infrastructure.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CalculationMetadataProperties.class)
public class CalculationMetadataConfiguration {
}
