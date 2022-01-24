package com.example.lecture2.websocket_stomp_rabbitmq.controller;

import com.example.lecture2.websocket_stomp_rabbitmq.model.ChatDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@Slf4j
public class StompRabbitController {
    private final RabbitTemplate template;

    private final static String CHAT_EXCHANGE_NAME = "chat.exchange";
    private final static String CHAT_QUEUE_NAME = "chat.queue";

    @MessageMapping("chat.enter.{chatRoomId}")
    public void enter(ChatDto chat, @DestinationVariable String chatRoomId){

        chat.setMessage("입장하셨습니다.");
        chat.setRegDate(LocalDateTime.now());

        template.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId, chat); // exchange
        //template.convertAndSend("room." + chatRoomId, chat); //queue
        //template.convertAndSend("amq.topic", "room." + chatRoomId, chat); //topic
    }

    @MessageMapping("chat.message.{chatRoomId}")
    public void send(ChatDto chat, @DestinationVariable String chatRoomId){
        chat.setRegDate(LocalDateTime.now());
        template.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId, chat);

        /*
        //chatRoomId = 1일 때
        //room.1이라는 이름의 Queue를 생성하고 구독한다.
        //단, RabbitMQ의 default Exchange(AMQP Default)와 바인딩 된다.
        //바인딩 키는 queue의 이름과 동일하다.
        //이거하면 채팅이 안된다. 4개의 클라이언트 중 1개의 클라이언트로 밖에 메세지가 안감.
        //이유는 AMQP Default의 type이 direct이기 때문이다.

        template.convertAndSend( "room." + chatRoomId, chat);
         */

        /*
        // /topic/<name> 의 형태
        //if: chatRoomId = 1
        //'amq.topic'이라는 Rabbit이 준비해둔 Exchange에 바인딩되는데, 바인딩 되는 Queue는 임의적인
        //이름을 가지며, Binding_key는 room.1이다.
        //exchange와 마찬가지로 클라이언트 당 1개의 Queue가 생긴다.
        //이 때 생성되는 Queue는 auto_deleted하고, durable하며 이름은 subscription-xxx...와 같이 생성된다

        template.convertAndSend("amq.topic", "room." + chatRoomId, chat);
         */
    }

    //receive()는 단순히 큐에 들어온 메세지를 소비만 한다. (현재는 디버그용도)
    @RabbitListener(queues = CHAT_QUEUE_NAME)
    public void receive(ChatDto chat){
        System.out.println("received : " + chat.getMessage());
    }
}
