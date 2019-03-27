/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.websocket;

import com.advantech.model.UserProfile;
import com.advantech.service.UserProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 *
 * @author Wei.Cheng
 */
public class ExampleHandler implements WebSocketHandler {

    private final Set<WebSocketSession> sessions = new HashSet();

    @Autowired
    private UserProfileService service;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession wss) throws Exception {
        if (wss.isOpen()) {
            String sessionId = (String) wss.getAttributes().get("sessionId");
            System.out.println("ConnectionEstablished: " + sessionId);
            sessions.add(wss);
            wss.sendMessage(new TextMessage("ConnectionEstablished"));
        }
    }

    @Override
    public void handleMessage(WebSocketSession wss, WebSocketMessage<?> wsm) throws Exception {
        System.out.println(wsm.getPayload());

        if (wsm.getPayload() instanceof String) {
            String message = (String) wsm.getPayload();
            sessions.forEach(s -> {
                try {
                    s.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            });
        }

    }

    @Override
    public void handleTransportError(WebSocketSession wss, Throwable thrwbl) throws Exception {
        System.out.println("TransportError");
        sessions.remove(wss);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession wss, CloseStatus cs) throws Exception {
        System.out.println("ConnectionClosed");
        sessions.remove(wss);
        System.out.println("Sessions size: " + sessions.size());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
