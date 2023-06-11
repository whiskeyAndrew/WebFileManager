package com.tgm.telegrammodule.RabbitMQ.DTO;

import lombok.*;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RabbitMessageDTO {
    long id;
    String time;
    String senderName;
    String status;
    String message;
}
