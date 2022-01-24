package com.example.lecture1.websocket.controller;

import com.example.lecture1.websocket.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

/*
 *  Controller의 메서드는 message handling methods 임
 *  이 메서드들은 한 Client에게서 message를 수신한 다음, 다른 Client에게 broadcast
 *
 * WebSocketConfig에서
 * "/app"로 시작하는 대상이 있는 클라이언트에서 보낸 모든 메시지는 @MessageMapping 어노테이션이 달린 메서드로 라우팅 됩니다.
 * 예를 들어
 * "/app/chat.sendMessage" 인 메세지는 sendMessage()로 라우팅 되며
 * "/app/chat.addUser" 인 메시지는 addUser()로 라우팅됩니다.
 */
@Controller
public class ChatController {

    /*
    Payload 란 전송되는 데이터를 의미한다.
    데이터를 전송할 때, Header와 META 데이터, 에러 체크 비트 등과 같은 다양한 요소들을 함께 보내 데이터 전송 효율과 안정성을 높히게 된다.
    이때, 보내고자 하는 데이터 자체를 의미하는 것이 페이로드이다.
    예를 들어 택배 배송을 보내고 받을 때 택배 물건이 페이로드고 송장이나 박스 등은 부가적은 것이기 때문에 페이로드가 아니다.
    {
        "status":
        "from":"localhost",
        "to":"http://melonicedlatte.com/chatroom/1",
        "method":"GET",
        "data":{"message":"There is a cutty dog!"}
    }
    다음 JSON에서 페이로드는 "data"이다. 나머지는 통신을 하는데 있어 용이하게 해주는 부가적 정보들이다.
     */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

}
