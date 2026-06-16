package cs.rug.keievaluationservice.infrastructure.messaging.outbound;

import cs.rug.keievaluationservice.api.events.evaluationcompleted.KeiEvaluationCompletedEvent;
import cs.rug.keievaluationservice.application.out.EvaluationResultPublisher;
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
public class RabbitEvaluationResultPublisher implements EvaluationResultPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final EvaluationResultRabbitMqProperties properties;

    @Override
    public void publish(KeiEvaluationCompletedEvent event) {
        rabbitTemplate.send(
                properties.getExchangeName(),
                properties.getRoutingKey(),
                buildJsonMessage(event)
        );
    }

    private Message buildJsonMessage(KeiEvaluationCompletedEvent event) {
        try {
            return MessageBuilder
                    .withBody(objectMapper.writeValueAsBytes(event))
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .build();
        } catch (JacksonException exception) {
            throw new IllegalStateException("Failed to serialize KEI evaluation completed event.", exception);
        }
    }
}
