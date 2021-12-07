package com.dmbb.boardgame.cards.service.impl;

import com.dmbb.boardgame.cards.model.dto.ClientMessageDTO;
import com.dmbb.boardgame.cards.model.dto.ServerMessageDTO;
import com.dmbb.boardgame.cards.model.entity.User;
import com.dmbb.boardgame.cards.model.enums.ClientMessageType;
import com.dmbb.boardgame.cards.service.GameService;
import com.dmbb.boardgame.cards.service.MessageReceiverService;
import com.dmbb.boardgame.cards.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageReceiverServiceImpl implements MessageReceiverService {

    private final GameService gameService;
    private final UserService userService;

    @Override
    @Transactional
    public void messageFromUser(ClientMessageDTO messageDTO, String auth, String username, Integer gameId) {
        log.info("message from: " + username);
        //User user = userService.getUserFromBaseAuth(auth);
        User user = userService.getUserByUsername(username);

        switch (messageDTO.getType()) {
            case DRAW_CARD_FROM_DECK:
                gameService.drawCardFromDeck(user, gameId);
                break;
            case ASK_FOR_ACTIVE_GAME_UPDATE:
                gameService.findGameInProgressStatusAndSendToOnePlayer(user);
                break;
            default:
                throw new RuntimeException("Unsupported message type: " + messageDTO.getType());
        }

    }
}
