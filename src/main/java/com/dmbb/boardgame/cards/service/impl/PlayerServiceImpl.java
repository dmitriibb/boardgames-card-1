package com.dmbb.boardgame.cards.service.impl;

import com.dmbb.boardgame.cards.model.dto.*;
import com.dmbb.boardgame.cards.model.entity.Card;
import com.dmbb.boardgame.cards.model.entity.Game;
import com.dmbb.boardgame.cards.model.entity.Player;
import com.dmbb.boardgame.cards.model.enums.CardStatus;
import com.dmbb.boardgame.cards.service.PlayerService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Override
    public void notifyPlayers(Game game) {
        GameUpdateDTO gameDTO = new GameUpdateDTO();
        gameDTO.setActivePlayer(game.getActivePlayerId());
        gameDTO.setTable(game.getCards().stream()
                .filter(card -> card.getStatus() == CardStatus.TABLE)
                .map(Card::toDTO)
                .collect(Collectors.toList()));

        game.getPlayers()
                .forEach(player -> notifyPlayer(player, gameDTO, game.getPlayers()));
    }

    private void notifyPlayer(Player player, GameUpdateDTO gameDTO, Set<Player> allPlayers) {
        gameDTO.setMe(playerFullDTO(player));

        gameDTO.setOtherPlayers(allPlayers.stream()
                .filter(p -> !p.equals(player))
                .map(p -> enrichPlayerShortDTO(new PlayerShortDTO(), p))
                .collect(Collectors.toList()));
    }

    private PlayerShortDTO enrichPlayerShortDTO(PlayerShortDTO dto, Player player) {
        dto.setId(player.getId());
        dto.setName(player.getUser().getName());
        dto.setCoins(player.getCoins());
        dto.setPoints(player.getPoints());
        dto.setAnchors(player.getAnchors());
        dto.setCrosses(player.getCrosses());
        dto.setHouses(player.getHouses());
        dto.setSwords(player.getSwords());
        dto.setCardNumber(player.getCards().size());
        return dto;
    }

    private PlayerFullDTO playerFullDTO(Player player) {
        PlayerFullDTO dto = new PlayerFullDTO();
        enrichPlayerShortDTO(dto, player);
        dto.setCards(player.getCards().stream().map(Card::toDTO).collect(Collectors.toList()));
        return dto;
    }


}
