package com.lotu_us.usedbook.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class StompConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/api/chat/stomp-chating")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .withSockJS();  //stomp는 sockJS기반
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/api/chat/send");           //클라이언트로부터 메세지를 받을 api의 prefix를 설정한다.
                                                                    //Controller의 MessageMapping 메서드로 라우팅된다.
        registry.enableSimpleBroker("/api/chat/receive");   //해당 api를 구독하고있는 클라이언트에게 메시지를 전달한다.
    }
}
