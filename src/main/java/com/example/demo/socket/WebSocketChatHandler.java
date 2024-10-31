package com.example.demo.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;


@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final ObjectMapper mapper;

    private WebSocketSession session1;
    private WebSocketSession session2;
    int num = 0;

    private ConcurrentHashMap<WebSocketSession, Ship> sessions = new ConcurrentHashMap<>();

    // 소켓 통신 시 메시지 전송을 handle
    // 받은 정보를 바탕으로, x와 y좌표 갱신
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception
    {
        if(session1 != null) System.out.println(session1.getId());
        if(session2 != null) System.out.println(session2.getId());
        if(sessions.containsKey(session))//이미있으면.
        {
            Ship ship = sessions.get(session);
            String payload = message.getPayload();

            GameRequestDTO gameDTO = mapper.readValue(payload, GameRequestDTO.class);

            if(gameDTO.getOrder().equals("LEFT"))
            {
                ship.setX(ship.getX()-10);
            }
            else if(gameDTO.getOrder().equals("RIGHT"))
            {
                ship.setX(ship.getX()+10);
            }else if(gameDTO.getOrder().equals("UP"))
            {
                ship.setY(ship.getY()-10);
            }else
            {
                ship.setY(ship.getY()+10);
            }
            WebSocketSession enemy = session == session1 ? session2 : session1;

            if(enemy == null)
            {
                session.sendMessage(new TextMessage(mapper.writeValueAsString(new GameResponseDTO(ship.getX(),ship.getY(), 0,0))));
            }
            else {
                Ship enemyShip = sessions.get(enemy);
                synchronized (session){
                    session.sendMessage(new TextMessage(mapper.writeValueAsString(new GameResponseDTO(ship.getX(),ship.getY(), enemyShip.getX(),enemyShip.getY()))));
                }
                synchronized (enemy){
                    enemy.sendMessage(new TextMessage(mapper.writeValueAsString(new GameResponseDTO(enemyShip.getX(),enemyShip.getY(), ship.getX(),ship.getY()))));
                }

            }

        }
        else //처음접속이면.
        {
            sessions.put(session, new Ship());
            if(num==0)
            {
                session1 = session;
                num++;
                Ship ship = sessions.get(session);
                String payload = message.getPayload();

                GameRequestDTO gameDTO = mapper.readValue(payload, GameRequestDTO.class);

                if(gameDTO.getOrder().equals("LEFT"))
                {
                    ship.setX(ship.getX()-10);
                }
                else if(gameDTO.getOrder().equals("RIGHT"))
                {
                    ship.setX(ship.getX()+10);
                }else if(gameDTO.getOrder().equals("UP"))
                {
                    ship.setY(ship.getY()-10);
                }else
                {
                    ship.setY(ship.getY()+10);
                }
                session.sendMessage(new TextMessage(mapper.writeValueAsString(new GameResponseDTO(ship.getX(),ship.getY(), 0,0))));
            }
            else
            {
                session2 = session;
                Ship ship = sessions.get(session);
                String payload = message.getPayload();

                GameRequestDTO gameDTO = mapper.readValue(payload, GameRequestDTO.class);

                if(gameDTO.getOrder().equals("LEFT"))
                {
                    ship.setX(ship.getX()-10);
                }
                else if(gameDTO.getOrder().equals("RIGHT"))
                {
                    ship.setX(ship.getX()+10);
                }else if(gameDTO.getOrder().equals("UP"))
                {
                    ship.setY(ship.getY()-10);
                }else
                {
                    ship.setY(ship.getY()+10);
                }

                Ship enemyShip = sessions.get(session1);
                session.sendMessage(new TextMessage(mapper.writeValueAsString(new GameResponseDTO(ship.getX(),ship.getY(), enemyShip.getX(),enemyShip.getY()))));
            }
        }


    }
}
