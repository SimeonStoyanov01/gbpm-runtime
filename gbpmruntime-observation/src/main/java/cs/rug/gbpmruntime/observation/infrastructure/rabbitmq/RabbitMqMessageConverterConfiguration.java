package cs.rug.gbpmruntime.observation.infrastructure.rabbitmq;

import org.springframework.amqp.support.converter.JacksonJavaTypeMapper;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqMessageConverterConfiguration {

    @Bean
    public MessageConverter rabbitMessageConverter() {
        JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter();
        converter.setTypePrecedence(JacksonJavaTypeMapper.TypePrecedence.INFERRED);
        converter.setAlwaysConvertToInferredType(true);
        return converter;
    }
}
