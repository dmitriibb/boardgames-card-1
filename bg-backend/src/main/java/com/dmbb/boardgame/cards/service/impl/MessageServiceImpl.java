package com.dmbb.boardgame.cards.service.impl;

import com.dmbb.boardgame.cards.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import static com.dmbb.boardgame.cards.config.Constants.TOPIC_MESSAGES;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendMessageToUser(String user, String message) {
        log.info("sending '" + message + "' to " + user);
        messagingTemplate.convertAndSendToUser(user, "/secured/user/queue/message", message);
    }

    @Override
    public void sendMessageToUser(String username, String destination, Object payload) {
        messagingTemplate.convertAndSendToUser(username, destination, payload);
    }
}
