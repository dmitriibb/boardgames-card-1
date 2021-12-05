package com.dmbb.boardgame.cards.service.impl;

import com.dmbb.boardgame.cards.config.Constants;
import com.dmbb.boardgame.cards.model.dto.*;
import com.dmbb.boardgame.cards.model.entity.Card;
import com.dmbb.boardgame.cards.model.entity.Game;
import com.dmbb.boardgame.cards.model.entity.Player;
import com.dmbb.boardgame.cards.model.enums.CardStatus;
import com.dmbb.boardgame.cards.service.MessageService;
import com.dmbb.boardgame.cards.service.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerServiceImpl implements PlayerService {

    private final MessageService messageService;

    @Override
    public void notifyPlayers(GameUpdateDTO gameUpdateDTO, Collection<Player> players) {

        Map<String, PlayerShortDTO> playersMap = new HashMap<>();
        List<PlayerShortDTO> allPlayers = new ArrayList<>();

        players.forEach(player -> {
            PlayerShortDTO dto = playerEntityToDTO(player);
            playersMap.put(player.getUser().getUsername(), dto);
            allPlayers.add(dto);
        });

        playersMap.forEach((username, playerDTO) -> sendGameUpdateDTOToPlayer(gameUpdateDTO, allPlayers, playerDTO, username));
    }

    @Override
    public PlayerShortDTO playerEntityToDTO(Player player) {
        PlayerShortDTO dto = new PlayerShortDTO();
        dto.setId(player.getId());
        dto.setName(player.getUser().getName());
        dto.setCoins(player.getCoins());
        dto.setPoints(player.getPoints());
        dto.setAnchors(player.getAnchors());
        dto.setCrosses(player.getCrosses());
        dto.setHouses(player.getHouses());
        dto.setSwords(player.getSwords());
        dto.setCards(player.getCards()
                .stream()
                .filter(card -> card.getStatus() == CardStatus.PLAYER_TABLE)
                .map(Card::toDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    private void sendGameUpdateDTOToPlayer(GameUpdateDTO gameUpdateDTO, List<PlayerShortDTO> allPlayers,
                                           PlayerShortDTO playerShortDTO, String username) {
        gameUpdateDTO.setMe(playerShortDTO);
        gameUpdateDTO.setOtherPlayers(copyPlayersDTOListExclude(allPlayers, playerShortDTO));
        messageService.sendMessageToUser(username, Constants.TOPIC_MESSAGES, gameUpdateDTO);
    }

    private List<PlayerShortDTO> copyPlayersDTOListExclude(List<PlayerShortDTO> list, PlayerShortDTO excludeDTO) {
        return list.stream()
                .filter(p -> p.getId() != excludeDTO.getId())
                .sorted(Comparator.comparing(PlayerShortDTO::getId))
                .collect(Collectors.toList());
    }


}
