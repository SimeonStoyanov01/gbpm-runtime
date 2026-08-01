package cs.rug.co2calculationservice.infrastructure.messaging.outbound;

import cs.rug.co2calculationservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;
import cs.rug.co2calculationservice.api.events.calculationfailed.KeiCalculationFailedEvent;
import cs.rug.co2calculationservice.application.out.CalculationResultPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class RabbitCalculationResultPublisher implements CalculationResultPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final CalculationResultRabbitMqProperties properties;

    @Override
    public void publishCompleted(KeiCalculationCompletedEvent event) {
        rabbitTemplate.send(
                properties.getExchangeName(),
                properties.getCompletedRoutingKey(),
                buildJsonMessage(event)
        );
    }

    @Override
    public void publishFailed(KeiCalculationFailedEvent event) {
        rabbitTemplate.send(
                properties.getExchangeName(),
                properties.getFailedRoutingKey(),
                buildJsonMessage(event)
        );
    }

    private Message buildJsonMessage(Object event) {
        try {
            return MessageBuilder
                    .withBody(objectMapper.writeValueAsBytes(event))
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .build();
        } catch (JacksonException exception) {
            throw new IllegalStateException("Failed to serialize KEI calculation completed event.", exception);
        }
    }
}
