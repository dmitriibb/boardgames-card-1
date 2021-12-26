package com.dmbb.boardgame.cards.service;

import com.dmbb.boardgame.cards.model.dto.GameUpdateDTO;
import com.dmbb.boardgame.cards.model.dto.PlayerShortDTO;
import com.dmbb.boardgame.cards.model.dto.ServerMessageDTO;
import com.dmbb.boardgame.cards.model.entity.Game;
import com.dmbb.boardgame.cards.model.entity.Player;
import com.dmbb.boardgame.cards.model.entity.User;

import java.util.Collection;
import java.util.List;

public interface PlayerService {

    void notifyPlayers(GameUpdateDTO gameUpdateDTO);

    void sendShortMessageToPlayers(Game game, String message);

    void sendMessageToUser(String username, String destination, ServerMessageDTO payload);

    Player getPlayerById(int id);

    void savePlayer(Player player);

    List<Player> getPlayersByGame(Game game);

    Player getPlayerByGameAndUser(User user, Game game);

    String getUsernameByPlayerId(int playerId);

    void sendErrorToUser(String username, String error);

    Player getNextPlayer(Game game, Player currentPlayer);

}
