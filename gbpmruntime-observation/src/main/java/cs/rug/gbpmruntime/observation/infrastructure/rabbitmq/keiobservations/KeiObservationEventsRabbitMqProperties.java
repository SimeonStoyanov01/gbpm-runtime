package cs.rug.gbpmruntime.observation.infrastructure.rabbitmq.keiobservations;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gbpmruntime.observation.messaging.kei-observation-events")
public class KeiObservationEventsRabbitMqProperties {

    private String exchangeName;

    private String routingKey;

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }
}
