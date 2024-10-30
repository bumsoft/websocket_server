package com.example.demo.socket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketHandler webSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
    {
        // endpoint : /chat
        // /chat으로 요청이 들어오면 websocket 통신 진행
        // setAllowedOrigins("*") : 모든 ip에서 접속 가능하도록.
        registry.addHandler(webSocketHandler, "/game").setAllowedOrigins("*");
    }
}
