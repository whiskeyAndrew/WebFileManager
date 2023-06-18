package com.FileManager.FileManager.RabbitMQ.service;

import com.FileManager.FileManager.RabbitMQ.DTO.RabbitMessageDTO;
import com.FileManager.FileManager.RabbitMQ.config.RabbitConfiguration;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class RabbitMessageSender extends Thread {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitConfiguration config;

    @PostConstruct
    private void init() {
//        this.start();
        rabbitTemplate.setReceiveTimeout(30000);
        rabbitTemplate.setReplyTimeout(30000);
        sendMessage("Файловый менеджер поднят");
    }

    public void sendMessage(String message) {
        try {
            RabbitMessageDTO messageDTO = RabbitMessageDTO.builder()
                    .id(1)
                    .time(Instant.now().toString())
                    .senderName("Server")
                    .message(message)
                    .build();
            rabbitTemplate.convertAndSend(config.getExchangeName(), config.getRoutingKey(), messageDTO);
        } catch (AmqpConnectException e){
            e.printStackTrace();
        }
    }

    public void run() {

//        try {
//            while (!isInterrupted()) {
//                Thread.sleep(2000);
//                sendMessage("Hello World");
//                Thread.sleep(2000);
//                sendMessage("Hello world but two");
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }
}
