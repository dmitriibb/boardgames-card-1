package com.dmbb.boardgame.cards.service.impl;

import com.dmbb.boardgame.cards.exception.ClientErrorException;
import com.dmbb.boardgame.cards.model.dto.CardClickDTO;
import com.dmbb.boardgame.cards.model.dto.ClientMessageDTO;
import com.dmbb.boardgame.cards.model.dto.ServerMessageDTO;
import com.dmbb.boardgame.cards.model.entity.User;
import com.dmbb.boardgame.cards.model.enums.ClientMessageType;
import com.dmbb.boardgame.cards.service.GameService;
import com.dmbb.boardgame.cards.service.MessageReceiverService;
import com.dmbb.boardgame.cards.service.PlayerService;
import com.dmbb.boardgame.cards.service.UserService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageReceiverServiceImpl implements MessageReceiverService {

    private final GameService gameService;
    private final UserService userService;
    private final PlayerService playerService;

    private final Executor executor = Executors.newCachedThreadPool();
    private final Gson gson = new Gson();

    @Override
    //@Transactional(propagation = Propagation.NESTED)
    public void messageFromUser(ClientMessageDTO messageDTO, String auth, String username, Integer gameId) {
        log.info("socket message from: " + username);
        //User user = userService.getUserFromBaseAuth(auth);
        User user = userService.getUserByUsername(username);

        try {
            processMessage(user, messageDTO, gameId);
        } catch (ClientErrorException e) {
            this.playerService.sendErrorToUser(username, e.getMessage());
        }

    }

    @Override
    public void messageFromUserREST(ClientMessageDTO messageDTO, User user, Integer gameId) {
        log.info("REST message from: " + user.getUsername());
        String username = user.getUsername();
        executor.execute(() -> {
            try {
                this.processMessage(user, messageDTO, gameId);
            } catch (ClientErrorException e) {
                this.playerService.sendErrorToUser(username, e.getMessage());
            }

        });
    }

    //@Transactional
    public void processMessage(User user, ClientMessageDTO messageDTO, Integer gameId) {
        CardClickDTO cardClickDTO;

        switch (messageDTO.getType()) {
            case DRAW_CARD_FROM_DECK:
                gameService.drawCardFromDeck(user, gameId);
                break;
            case ASK_FOR_ACTIVE_GAME_UPDATE:
                gameService.findGameInProgressStatusAndSendToOnePlayer(user);
                break;
            case SELL_SHIP:
                cardClickDTO = gson.fromJson(messageDTO.getPayload(), CardClickDTO.class);
                gameService.sellShip(user, gameId, cardClickDTO.getCardId());
                break;
            case DESTROY_SHIP:
                cardClickDTO = gson.fromJson(messageDTO.getPayload(), CardClickDTO.class);
                gameService.destroyShip(user, gameId, cardClickDTO.getCardId());
                break;
            case BUY_PERSON:
                cardClickDTO = gson.fromJson(messageDTO.getPayload(), CardClickDTO.class);
                gameService.buyPerson(user, gameId, cardClickDTO.getCardId());
                break;
            case PASS:
                gameService.playerPass(user, gameId);
                break;

            default:
                throw new RuntimeException("Unsupported message type: " + messageDTO.getType());
        }
    }
}
