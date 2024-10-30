package com.example.demo.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;




@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final ObjectMapper mapper;
    static int x=100;
    static int y=100;

    // 소켓 통신 시 메시지 전송을 handle
    // 받은 정보를 바탕으로, x와 y좌표 갱신
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception
    {
            String payload = message.getPayload();
        System.out.println(payload);

            // 페이로드 -> gameDto로 변환
            GameRequestDTO gameDTO = mapper.readValue(payload, GameRequestDTO.class);



            if(gameDTO.getOrder().equals("LEFT"))
            {
                x-=10;
            }
            else if(gameDTO.getOrder().equals("RIGHT"))
            {
                x+=10;
            }else if(gameDTO.getOrder().equals("UP"))
            {
                y-=10;
            }else
            {
                y+=10;
            }
            session.sendMessage(new TextMessage(mapper.writeValueAsString(new GameResponseDTO(x,y))));
    }
}
