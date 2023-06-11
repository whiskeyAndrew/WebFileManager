package com.tgm.telegrammodule.RabbitMQ.service;

import com.tgm.telegrammodule.RabbitMQ.DTO.RabbitMessageDTO;
import com.tgm.telegrammodule.RabbitMQ.config.RabbitConfiguration;
import com.tgm.telegrammodule.Telegram.TrashcanTelegramBot;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageReceiver {
    private final RabbitConfiguration config;
    private final TrashcanTelegramBot bot;

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void handleMessage(RabbitMessageDTO message){
        log.info("Received " + message);
        bot.sendMessageToAuthorizedUsers(message.getMessage());
    }
}
