package com.example.lecture1.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

/**
 * Created by rajeevkumarsingh on 24/07/17.
 */
@Configuration
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /*
    클라이언트가 웹 소켓 서버에 연결하는 데 사용할 웹 소켓 엔드 포인트를 등록
    엔드 포인트 구성에 withSockJS ()를 사용
    SockJS는 웹 소켓을 지원하지 않는 브라우저에 fallback 옵션을 활성화하는 데 사용
    - fallback - 어떤 기능이 약해지거나 제대로 동작하지 않을 때 대처하는 동작
    - SockJS : Native WebSocket을 사용하려고하는 WebSocket Client이며 WebSocket을 지원하지 않는 구형 브라우저에 대체 옵션을 제공
    - STOMP : Simple Text Oriented Messaging Protocol (데이터 교환의 형식과 규칙을 정의하는 메시징 프로토콜)
        pub / sub란 메세지를 공급하는 주체와 소비하는 주체를 분리해 제공하는 메세징 방법이다.

        기본적인 컨셉을 예로 들자면 우체통(Topic)이 있다면 집배원(Publisher)이 신문을 우체통에 배달하는 행위가 있고,
        우체통에 신문이 배달되는 것을 기다렸다가 빼서 보는 구독자(Subscriber)의 행위가 있다.
        이때 구독자는 다수가 될 수 있다.

        pub / sub 컨셉을 채팅방에 빗대면 다음과 같다.
        채팅방 생성 : pub / sub 구현을 위한 Topic이 생성됨
        채팅방 입장 : Topic 구독
        채팅방에서 메세지를 송수신 : 해당 Topic으로 메세지를 송신(pub), 메세지를 수신(sub)

        구독 메세지 구조
         ---------------------------------------------------------------------
        SUBSCRIBE
        destination: /topic/public
        id: sub-1

        ^@
        ---------------------------------------------------------------------

        SEND 메세지 구조
         ---------------------------------------------------------------------
        SEND
        destination: /app/chat.sendMessage
        content-type: application/json

        {"type": "CHAT", "content": "메세지입니다.", "sender": "glamrock"} ^@
         ---------------------------------------------------------------------

        broadcast 메세지 구조
         ---------------------------------------------------------------------
        MESSAGE
        destination: /topic/public
        message-id: d4c0d7f6-1
        subscription: sub-1

        {"type": "CHAT", "content": "메세지입니다.", "sender": "glamrock"} ^@
         ---------------------------------------------------------------------

         STOMP의 "destination" 헤더를 기반으로 @Controller 객체의 @MethodMapping 메서드로 라우팅할 수 있어
         WebSocket 기반으로 각 Connection(연결)마다 WebSocketHandler를 구현하는 것 보다 @Controller 된 객체를 이용해 관리가 가능
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // "/ws"는 WebSocket 또는 SockJS Client가 웹소켓 핸드셰이크 커넥션을 생성할 경로
        registry.addEndpoint("/ws").withSockJS();
    }

    /*
    스프링은 메세지를 외부 Broker에게 전달하고, Broker는 WebSocket으로 연결된 클라이언트에게 메세지를 전달하는 구조

    한 클라이언트에서 다른 클라이언트 (또는 서버에서 여러 클라이언트) 로 메시지를 라우팅 하는 데 사용될 메시지 브로커를 구성
    클라이언트가 메시지 브로커를 subscribe 하고 서버가 publishing 하는 구조
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        /*
            "/app" 시작되는 메시지가 message-handling methods으로 라우팅 되어야 한다는 것을 명시
            -> "/app" 경로로 시작하는 STOMP 메세지의 "destination" 헤더는 @Controller 객체의 @MessageMapping 메서드로 라우팅
            spring-message 의 SimpleAnnotationMethod : @MessageMapping 등 Client의 STOMP 메세지의 SEND를 받아서 처리
         */
        registry.setApplicationDestinationPrefixes("/app");

        /*
             "/topic" 시작되는 메시지가 메시지 브로커로 라우팅 되도록 정의
             -> "/topic"으로 시작하는 "destination" 헤더를 가진 STOMP 메세지를 브로커로 라우팅

             Simple In-Memory Broker 메시지 브로커를 활성화
             Spring에서 지원하는 STOMP는 많은 기능을 하는데 예를 들어 Simple In-Memory Broker를 이용해 SUBSCRIBE 중인 다른 클라이언트들에게 메세지를 보내준다.
             Simple In Memory Broker는 클라이언트의 SUBSCRIBE 정보를 자체적으로 메모리에 유지한다.

             RabbitMQ 또는 ActiveMQ와 같은 다른 모든 기능을 갖춘 메시지 브로커를 자유롭게 사용가능
             message broker 가 topic 을 구독한 모든 client 에 메세지를 broadcast

             prefix 는 일반적으로 다음과 같은 구조를 따름
             "topic/.." --> publish-subscribe (1:N)
             "queue/" --> point-to-point (1:1)
         */
        registry.enableSimpleBroker("/topic");   // Enables a simple in-memory broker


        //   RabbitMQ 를 message broker 로 사용할 경우
        /*
        registry.enableStompBrokerRelay("/topic")
                .setRelayHost("localhost")
                .setRelayPort(61613)
                .setClientLogin("guest")
                .setClientPasscode("guest");
        */
    }
}
