package com.ggomak.springboot2.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private List<WebSocketSession> sessionList = new ArrayList<WebSocketSession>();

    // 클라이언트와 연결 이후에 실행되는 메서드
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessionList.add(session);
        System.out.printf("{%s} 연결됨\n", session.getId());
        System.out.printf("주소 : %s\n", session.getLocalAddress());

        session.sendMessage(new TextMessage("CONNECT"));    // 웹 소켓 접속 성공
        session.sendMessage(new TextMessage("READY"));      // 웹캠 촬영 대기
    }

    // 클라이언트가 서버로 메시지를 전송했을 때 실행되는 메서드
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        System.out.printf("{%s} 로 부터 검출점 받음\n", session.getId(), message.getPayload());
        System.out.printf("%s", message.getPayload());
        session.sendMessage(new TextMessage("SUCCESS"));

        /*
        for (WebSocketSession sess : sessionList) {
            sess.sendMessage(new TextMessage(session.getId() + " : " + message.getPayload()));
        }
        */
    }

    // 클라이언트와 연결을 끊었을 때 실행되는 메소드
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionList.remove(session);
        System.out.printf("{%s} 연결 끊김\n", session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        session.sendMessage(new TextMessage("FAIL"));
    }
}
