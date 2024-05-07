package isi.dan.mspago.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Configuration
public class ConfigureRabbitMq {

    public static final String QUEUE_NAME = "pedidos";
    public static final String EXCHANGE_NAME = "pedidos";
    public static final String ROUTING_KEY = "pedidos";

    @Bean
    @Primary
    Queue createQueue() {
        return new Queue(QUEUE_NAME, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }

    @Bean
    Queue respuestaQueue() {
        return new Queue("respuesta.pedidos", false);
    }

    @Bean
    Binding respuestaBinding(Queue respuestaQueue, TopicExchange exchange) {
        return BindingBuilder.bind(respuestaQueue).to(exchange).with("respuesta.pedidos");
    }
    @Component
public class ResponseListener {

    @RabbitListener(queues = "respuesta.pedidos")
    public void receiveResponse(String response) {
        System.out.println("Respuesta recibida del microservicio de pedidos: " + response);
        // Aquí puedes agregar la lógica para procesar la respuesta recibida, si es necesario
    }
}
}