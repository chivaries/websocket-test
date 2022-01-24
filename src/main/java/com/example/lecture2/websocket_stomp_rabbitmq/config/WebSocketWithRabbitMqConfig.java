package com.example.lecture2.websocket_stomp_rabbitmq.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/*
채팅 서버를 구현하면서 외부의 메세지 브로커를 두어야 하는 이유
- 로드밸런싱 시, 채팅 서버는 스프링 프로젝트에 종속적이기 때문에 같은 채팅방에 있다 하더라도 같은 내용의 채팅을 볼 수 없다.
- 동시접속자가 특정 범위를 초과하면(컨테이너가 수용할 수 있는 수를 초과하면) 다른 컨테이너의 서버로 켜지기 때문에 동일한 채팅을 볼 수 없다.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketWithRabbitMqConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp/chat")
                .setAllowedOrigins("http://*.*.*.*:8081", "http://*:8081") //안해도 무관
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setPathMatcher(new AntPathMatcher(".")); // url을 chat/room/3 -> chat.room.3으로 참조하기 위한 설정
        registry.setApplicationDestinationPrefixes("/pub");
        //registry.enableSimpleBroker("/sub");
        registry.enableStompBrokerRelay("/queue", "/topic", "/exchange", "/amq/queue");
    }
}
