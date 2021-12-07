package com.dmbb.boardgame.cards.service;

import com.dmbb.boardgame.cards.model.dto.GameUpdateDTO;
import com.dmbb.boardgame.cards.model.dto.PlayerShortDTO;
import com.dmbb.boardgame.cards.model.entity.Game;
import com.dmbb.boardgame.cards.model.entity.Player;
import com.dmbb.boardgame.cards.model.entity.User;

import java.util.Collection;

public interface PlayerService {

    void notifyPlayers(GameUpdateDTO gameUpdateDTO);

    void sendMessageToUser(String username, String destination, Object payload);

    Player getPlayerById(int id);

    String getUsernameByPlayerId(int playerId);

}
