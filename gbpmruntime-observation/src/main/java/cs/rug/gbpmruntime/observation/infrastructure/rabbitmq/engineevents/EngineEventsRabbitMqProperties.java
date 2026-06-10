package cs.rug.gbpmruntime.observation.infrastructure.rabbitmq.engineevents;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gbpmruntime.observation.messaging.engine-events")
public class EngineEventsRabbitMqProperties {

    private String queueName;

    private List<Binding> bindings = new ArrayList<>();

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public List<Binding> getBindings() {
        return bindings;
    }

    public void setBindings(List<Binding> bindings) {
        this.bindings = bindings;
    }

    public static class Binding {

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
}
