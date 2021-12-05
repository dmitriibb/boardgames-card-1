package com.dmbb.boardgame.cards.service;

import com.dmbb.boardgame.cards.model.dto.GameUpdateDTO;
import com.dmbb.boardgame.cards.model.dto.PlayerShortDTO;
import com.dmbb.boardgame.cards.model.entity.Game;
import com.dmbb.boardgame.cards.model.entity.Player;

import java.util.Collection;

public interface PlayerService {

    void notifyPlayers(GameUpdateDTO gameUpdateDTO, Collection<Player> players);

    PlayerShortDTO playerEntityToDTO(Player player);

}
