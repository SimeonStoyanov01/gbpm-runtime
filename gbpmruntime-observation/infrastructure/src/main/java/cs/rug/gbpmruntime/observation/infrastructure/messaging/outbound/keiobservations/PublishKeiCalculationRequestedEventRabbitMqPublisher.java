package cs.rug.gbpmruntime.observation.infrastructure.messaging.outbound.keiobservations;

import cs.rug.gbpmruntime.observation.api.events.keicalculationrequested.KeiCalculationRequestedEvent;
import cs.rug.gbpmruntime.observation.application.out.KeiCalculationRequestedEventPublisher;
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
public class PublishKeiCalculationRequestedEventRabbitMqPublisher
        implements KeiCalculationRequestedEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final KeiObservationEventsRabbitMqProperties rabbitMqProperties;

    @Override
    public void publish(KeiCalculationRequestedEvent event) {
        rabbitTemplate.send(
                rabbitMqProperties.getExchangeName(),
                rabbitMqProperties.getCalculationRequestedRoutingKey(),
                buildJsonMessage(event)
        );
    }

    private Message buildJsonMessage(KeiCalculationRequestedEvent event) {
        try {
            return MessageBuilder
                    .withBody(objectMapper.writeValueAsBytes(event))
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .build();
        } catch (JacksonException exception) {
            throw new IllegalStateException("Failed to serialize KEI calculation requested event.", exception);
        }
    }
}
