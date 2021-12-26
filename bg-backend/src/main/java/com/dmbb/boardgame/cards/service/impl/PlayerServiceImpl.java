package com.dmbb.boardgame.cards.service.impl;

import com.dmbb.boardgame.cards.config.Constants;
import com.dmbb.boardgame.cards.model.dto.*;
import com.dmbb.boardgame.cards.model.entity.Card;
import com.dmbb.boardgame.cards.model.entity.Game;
import com.dmbb.boardgame.cards.model.entity.Player;
import com.dmbb.boardgame.cards.model.entity.User;
import com.dmbb.boardgame.cards.model.enums.CardStatus;
import com.dmbb.boardgame.cards.model.enums.ServerMessageType;
import com.dmbb.boardgame.cards.repository.PlayerRepository;
import com.dmbb.boardgame.cards.service.PlayerService;
import com.dmbb.boardgame.cards.util.MyUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerServiceImpl implements PlayerService {

    private final SimpMessagingTemplate messagingTemplate;
    private final PlayerRepository playerRepository;

    @Override
    public void notifyPlayers(GameUpdateDTO gameUpdateDTO) {
        List<PlayerShortDTO> allPlayers = gameUpdateDTO.getOtherPlayers();

        allPlayers.forEach(player -> {
            String username = player.getUsername();
            List<PlayerShortDTO> otherPlayers = MyUtils.copyPlayersDTOListExclude(allPlayers, player.getId());
            gameUpdateDTO.setMe(player);
            gameUpdateDTO.setOtherPlayers(otherPlayers);
            sendGameUpdateDTOToPlayer(username, gameUpdateDTO);
        });
    }

    @Override
    public void sendShortMessageToPlayers(Game game, String message) {
        List<String> usernames = playerRepository.findUsernamesByGame(game);
        usernames.forEach(username -> {
            ServerMessageDTO messageDTO = new ServerMessageDTO(ServerMessageType.SHORT_MESSAGE, message);
            sendMessageToUser(username, Constants.TOPIC_MESSAGES, messageDTO);
        });
    }

    private void sendGameUpdateDTOToPlayer(String username, GameUpdateDTO gameUpdateDTO) {
        ServerMessageDTO messageDTO = new ServerMessageDTO(ServerMessageType.GAME_UPDATE, gameUpdateDTO);
        sendMessageToUser(username, Constants.TOPIC_MESSAGES, messageDTO);
    }

    @Override
    public void sendMessageToUser(String username, String destination, ServerMessageDTO payload) {
        try {
            messagingTemplate.convertAndSendToUser(username, destination, payload);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    @Override
    public Player getPlayerById(int id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player is not found for id: " + id));
    }

    @Override
    public void savePlayer(Player player) {
        playerRepository.save(player);
    }

    @Override
    public List<Player> getPlayersByGame(Game game) {
        return playerRepository.getAllByGameOrderByPlayerOrder(game);
    }

    @Override
    public Player getPlayerByGameAndUser(User user, Game game) {
        return playerRepository.findByUserAndGame(user, game)
                .orElseThrow(() -> new RuntimeException("Player is not found for user: " + user.getUsername() + " and game id: " + game.getId()));
    }

    @Override
    public String getUsernameByPlayerId(int playerId) {
        return playerRepository.getUsernameByPlayerId(playerId);
    }

    @Override
    public void sendErrorToUser(String username, String error) {
        ServerMessageDTO messageDTO = new ServerMessageDTO(ServerMessageType.ERROR, error);
        sendMessageToUser(username, Constants.TOPIC_MESSAGES, messageDTO);
    }

    @Override
    public Player getNextPlayer(Game game, Player currentPlayer) {
        Player nextPlayer = playerRepository.findFirstByGameAndPlayerOrderGreaterThanOrderByPlayerOrder(game, currentPlayer.getPlayerOrder());
        if (nextPlayer == null)
            nextPlayer = playerRepository.findFirstByGameOrderByPlayerOrder(currentPlayer.getGame());

        return nextPlayer;
    }


}
