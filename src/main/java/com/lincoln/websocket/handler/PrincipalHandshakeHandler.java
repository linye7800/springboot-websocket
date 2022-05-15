package com.lincoln.websocket.handler;

import java.security.Principal;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

/**
 * description:
 *
 * @author linye
 * @date 2022年05月15日 5:04 PM
 */
@Slf4j
@Component
public class PrincipalHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(
            ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        /**
         * 这边可以按你的需求，如何获取唯一的值，既unicode
         * 得到的值，会在监听处理连接的属性中，既WebSocketSession.getPrincipal().getName()
         * 也可以自己实现Principal()
         */
        // instanceof 是java的保留关键字。他的作用就是测试左边的对象是不是右边类的实例，是的话就返回true，不是的话返回false。
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
            HttpServletRequest httpServletRequest = servletServerHttpRequest.getServletRequest();
            /**
             * 这边就获取你最熟悉的陌生人,携带参数，你可以cookie，请求头，或者url携带，这边我采用url携带
             */
            final String token = httpServletRequest.getParameter("token");
            if (StringUtils.isEmpty(token)) {
                return null;
            }
//            简写前
//            return new Principal() {
//                @Override
//                public String getName() {
//                    return token;
//                }
//            };
            return () -> token;
        }
        return null;
    }

}
