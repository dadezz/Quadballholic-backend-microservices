package com.quadballholic.backend.livegameevents.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Events will be pushed to destinations starting with /topic
        config.enableSimpleBroker("/topic");
        // Clients will send requests (like subscribing) to /app
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // The endpoint the frontend connects to
        registry.addEndpoint("/ws-quadball")
                .setAllowedOriginPatterns("*") // Configure safely for prod
                .withSockJS(); // Fallback for older browsers
    }
}