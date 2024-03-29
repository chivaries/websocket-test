package com.example.lecture2.websocket_stomp.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class ChatRoomDto {
    private String roomId;
    private String name;

    //WebSocketSession은 Spring에서 Websocket Connection이 맺어진 세션
    private Set<WebSocketSession> sessions = new HashSet<>();

    public static ChatRoomDto create(String name){
        ChatRoomDto room = new ChatRoomDto();
        room.roomId = UUID.randomUUID().toString();
        room.name = name;
        return room;
    }
}
