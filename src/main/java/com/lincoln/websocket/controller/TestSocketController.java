package com.lincoln.websocket.controller;

import com.alibaba.fastjson.JSON;
import com.lincoln.websocket.handler.SocketManager;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketSession;

/**
 * description:
 *
 * @author linye
 * @date 2022年05月15日 10:20 PM
 */
@Slf4j
@RestController
public class TestSocketController {

    @Autowired
    private SimpMessagingTemplate template;

    /**
     * 服务器指定用户进行推送,需要前端开通 var socket = new SockJS(host+'/myUrl' + '?token=1234');
     */
    @RequestMapping("/sendUser")
    public void sendUser(String token) {
        log.info("token = {}, 对其发送您好", token);
        WebSocketSession webSocketSession = SocketManager.get(token);
        if (Objects.nonNull(webSocketSession)) {
            /**
             * 主要防止broken pipe
             */
            template.convertAndSendToUser(token, "/queue/sendUser", "您好");
        }
    }

    /**
     * 广播，服务器端主动推给连接的客户端
     */
    @RequestMapping("/sendTopic")
    public void sendTopic() {
        template.convertAndSend("/topic/sendTopic", "大家晚上好");
    }

    /**
     * 客户端发消息，服务端接收
     * 相当于RequestMapping
     * @param message
     */
    @MessageMapping("/sendServer")
    public void sendServer(String message) {
        log.info("message: {}", message);
    }

    /**
     * 客户端发消息，大家都接收，相当于直播说话
     * @param message
     * @return
     */
    @MessageMapping("/sendAllUser")
    @SendTo("/topic/sendTopic")
    public String sendAllUser(String message) {
        // 也可以采用temple方式
        return message;
    }

    /**
     * 点对点用户聊天，这边需要注意，由于前端传过来的json数据，所以使用@RequestBody来接收
     * 这边需要点断开通var socket = new SockJS(host+"/myUrl" + "?token=45678"); token为指定的name
     * @param map
     */
    @MessageMapping("/sendMyUser")
    public void sendMyUser(@RequestBody Map<String, String> map) {
        log.info("map = {} ", JSON.toJSONString(map));
        WebSocketSession webSocketSession = SocketManager.get(map.get("name"));
        if (Objects.nonNull(webSocketSession)) {
            log.info("sessionId = {}", webSocketSession.getId());
            template.convertAndSendToUser(map.get("name"), "/queue/sendUser", map.get("message"));
        }
    }

}
