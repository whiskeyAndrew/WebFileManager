package com.FileManager.FileManager.RabbitMQ.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class RabbitMessageDTO {
    long id;
    String time;
    String senderName;
    String status;
    String message;
}
