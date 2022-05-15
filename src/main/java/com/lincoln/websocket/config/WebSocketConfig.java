package com.lincoln.websocket.config;

import com.lincoln.websocket.factory.WebSocketDecoratorFactory;
import com.lincoln.websocket.handler.PrincipalHandshakeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

/**
 * description:
 * WebSocketConfig配置
 * @author linye
 * @date 2022年05月15日 5:00 PM
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Autowired
    private WebSocketDecoratorFactory webSocketDecoratorFactory;

    @Autowired
    private PrincipalHandshakeHandler principalHandshakeHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        /**
         * myUrl表示 前端到时候要对应的url映射
         */
        registry.addEndpoint("myUrl")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(principalHandshakeHandler)
                .withSockJS();
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registration) {
        registration.enableSimpleBroker("queue","/topic");
        registration.setUserDestinationPrefix("user");
    }

    public void configureSocketTransport(WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(webSocketDecoratorFactory);
        super.configureWebSocketTransport(registration);
    }
}
