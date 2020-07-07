/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.websocket;

import com.advantech.helper.SecurityPropertiesUtils;
import com.advantech.model.db1.User;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 *
 * @author Wei.Cheng
 */
public class WebSocketInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest shr, ServerHttpResponse shr1, WebSocketHandler wsh, Map<String, Object> map) throws Exception {
//        User user = SecurityPropertiesUtils.retrieveAndCheckUserInSession();
//        if (user != null) {
//            map.put("user", user);
//        }

        if (shr instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) shr;
            HttpSession session = servletRequest.getServletRequest().getSession();
            map.put("sessionId", session.getId());
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest shr, ServerHttpResponse shr1, WebSocketHandler wsh, Exception excptn) {

    }

}
