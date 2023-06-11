package com.FileManager.FileManager.RabbitMQ.config;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.Getter;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import java.util.HashMap;


//Сделал ради тестов два способа создания соединения
//Здесь все работает без connectionFactory (скорее всего оно где-то под капотом там тоже создается)
//Этот класс самостоятельно настраивает в кролике нужный exchange и привязывает к нему очередь
//И затем начинает по exchangeName слать данные в MessageSender
//Смешно, что можно и так и так это все сделать и оно будет работать

@Configuration
public class RabbitConfiguration {

    @Getter
    private final String exchangeName = "tg.file-manager.exchange";
    @Getter
    private final String queueName = "tg.file-manager.queue";

    @Getter
    private final String routingKey = "file-manager.actions.logs";

    @Bean
    Queue queue() {
        //true - очередь остается после ребута кролика
        //false - очередь умирает после ребута кролика
        Queue queue = new Queue(queueName,true);
        return new Queue(queueName, true, false, false,
                new HashMap<>() {{
                    put("x-message-ttl", 1000); // TTL в миллисекундах
                }});
    }

    @Bean
    TopicExchange exchange(){
        return new TopicExchange(exchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange topicExchange){
        return BindingBuilder.bind(queue).to(topicExchange).with(routingKey);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
