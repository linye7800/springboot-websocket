package com.lincoln.websocket.factory;

import com.lincoln.websocket.handler.SocketManager;
import java.security.Key;
import java.security.Principal;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

/**
 * description:
 * 首先，我们需要重写WebSocketHandlerDecoratorFactory
 * 主要用来监控客户端握手连接进来以及挥手关闭连接
 * 服务端和客户端在进行握手挥手时会被执行
 *
 * 注意：以下session变量，需要注意两点，一个是getId(),一个是getPrincipal().getName()
 * getId() ： 返回的是唯一的会话标识符。
 * getPrincipal() : 经过身份验证，返回Principal实例，未经过身份验证，返回null
 * Principal: 委托人的抽象概念,可以是公司id，名字，用户唯一识别token等
 *
 * 当你下面代码使用，你会发现getPrincipal()返回null，为什么？这边还需要重写一个DefaultHandshakeHandler
 *
 * @author linye
 * @date 2022年05月15日 4:33 PM
 */
@Slf4j
@Component
public class WebSocketDecoratorFactory implements WebSocketHandlerDecoratorFactory {

    @Override
    public WebSocketHandler decorate(WebSocketHandler handler) {
        return new WebSocketHandlerDecorator(handler) {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                log.info("有人开始连接啦 sessionId = {}", session.getId());
                Principal principal = session.getPrincipal();
                if (Objects.nonNull(principal)) {
                    log.info("key = {} 存入", principal.getName());
                    // 身份校验成功，缓存socket连接
                    SocketManager.add(principal.getName(), session);
                }
                super.afterConnectionEstablished(session);
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                log.info("有人退出连接啦 sessionId = {}，让他走吧，他还会回来的", session.getId());
                Principal principal = session.getPrincipal();
                if (Objects.nonNull(principal)) {
                    // 身份校验成功，移除socket连接
                    SocketManager.remove(principal.getName());
                }
                super.afterConnectionClosed(session, closeStatus);
            }
        };
    }
}
