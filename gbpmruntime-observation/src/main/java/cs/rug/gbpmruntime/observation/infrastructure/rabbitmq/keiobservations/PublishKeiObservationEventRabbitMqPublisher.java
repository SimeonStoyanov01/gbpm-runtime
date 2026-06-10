package cs.rug.gbpmruntime.observation.infrastructure.rabbitmq.keiobservations;

import cs.rug.gbpmruntime.observation.api.operations.publishkeiobservation.PublishKeiObservationEventOperation;
import cs.rug.gbpmruntime.observation.api.operations.publishkeiobservation.PublishKeiObservationEventRequest;
import cs.rug.gbpmruntime.observation.api.operations.publishkeiobservation.PublishKeiObservationEventResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class PublishKeiObservationEventRabbitMqPublisher implements PublishKeiObservationEventOperation {

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final String routingKey;

    public PublishKeiObservationEventRabbitMqPublisher(
            RabbitTemplate rabbitTemplate,
            KeiObservationEventsRabbitMqProperties properties
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = properties.getExchangeName();
        this.routingKey = properties.getRoutingKey();
    }

    @Override
    public PublishKeiObservationEventResponse process(PublishKeiObservationEventRequest request) {
        rabbitTemplate.convertAndSend(
                exchangeName,
                routingKey,
                request.getEvent()
        );

        return PublishKeiObservationEventResponse
                .builder()
                .eventId(request.getEvent().getEventId())
                .published(true)
                .build();
    }
}
