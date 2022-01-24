package com.example.lecture2.websocket_stomp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {
    private String roomId;
    private String writer;
    private String message;
}
