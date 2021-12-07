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

    private void sendGameUpdateDTOToPlayer(String username, GameUpdateDTO gameUpdateDTO) {
        ServerMessageDTO messageDTO = new ServerMessageDTO(ServerMessageType.GAME_UPDATE, gameUpdateDTO);
        sendMessageToUser(username, Constants.TOPIC_MESSAGES, messageDTO);
    }

    @Override
    public void sendMessageToUser(String username, String destination, Object payload) {
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
    public String getUsernameByPlayerId(int playerId) {
        return playerRepository.getUsernameByPlayerId(playerId);
    }


}
