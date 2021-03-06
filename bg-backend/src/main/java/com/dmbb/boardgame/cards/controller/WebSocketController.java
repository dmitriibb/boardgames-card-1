package com.dmbb.boardgame.cards.controller;

import com.dmbb.boardgame.cards.exception.ClientErrorException;
import com.dmbb.boardgame.cards.model.dto.ClientMessageDTO;
import com.dmbb.boardgame.cards.model.dto.ServerMessageDTO;
import com.dmbb.boardgame.cards.model.entity.User;
import com.dmbb.boardgame.cards.service.MessageReceiverService;
import com.dmbb.boardgame.cards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;


import javax.transaction.Transactional;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final MessageReceiverService messageReceiverService;

    @MessageMapping("/user/message")
    public void messageFromUser(SimpMessageHeaderAccessor sha,
                                @Header("Authorization") String auth,
                                @Header("simpUser") Principal principal,
                                @Header(name = "gameId", required = false) Integer gameId,
                                @Payload ClientMessageDTO messageDTO) {

        messageReceiverService.messageFromUser(messageDTO, auth, principal.getName(), gameId);
    }



}
