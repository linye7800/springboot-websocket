package com.lincoln.websocket.handler;

import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

/**
 * description:
 *
 * @author linye
 * @date 2022年05月15日 4:21 PM
 */
@Slf4j
public class SocketManager {

    private static ConcurrentHashMap<String, WebSocketSession> manager = new ConcurrentHashMap<String, WebSocketSession>();

    /**
     * 添加webSocket连接
     * @param key
     * @param webSocketSession
     */
    public static void add(String key, WebSocketSession webSocketSession) {
        log.info("新添加的Websocket连接 {}", key);
        manager.put(key, webSocketSession);
    }

    /**
     * 移除webSocket连接
     * @param key
     */
    public static void remove(String key) {
        log.info("移除webSocket连接 {} ", key);
        manager.remove(key);
    }

    /**
     * 获取webSocket连接
     * @param key
     * @return
     */
    public static WebSocketSession get(String key) {
        log.info("获取webSocket连接 {}", key);
        return manager.get(key);
    }

}
