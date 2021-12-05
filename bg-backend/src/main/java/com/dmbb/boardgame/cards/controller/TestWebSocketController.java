package com.dmbb.boardgame.cards.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class TestWebSocketController {

    @MessageMapping("/hello")
    @SendTo("/topic/hello")
    public String testWs(SimpMessageHeaderAccessor sha,
                         @Payload String helloMessage,
                         @Header("simpSessionId") String sessionId) throws InterruptedException {
        log.info("hello from web socket: " + helloMessage);
        log.info("sessionId: " + sessionId);
        log.info("user: " + sha.getUser());

        Thread.sleep(500);
        return "Response from server for hello message from " + sessionId;
    }

    @MessageMapping("/secured/test")
    @SendTo("/topic/hello")
    public String testWs2(SimpMessageHeaderAccessor sha,
                         @Payload String helloMessage,
                         @Header("simpSessionId") String sessionId) throws InterruptedException {
        log.info("hello from web socket: " + helloMessage);
        log.info("sessionId: " + sessionId);
        log.info("user: " + sha.getUser());

        Thread.sleep(500);
        return "Response from server for secured/test message from " + sessionId;
    }

}
