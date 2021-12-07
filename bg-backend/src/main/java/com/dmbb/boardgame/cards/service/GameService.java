package com.dmbb.boardgame.cards.service;

import com.dmbb.boardgame.cards.model.dto.GameUpdateDTO;
import com.dmbb.boardgame.cards.model.entity.User;

public interface GameService {

    void startGame(User user, int gameId);

    void sendGameUpdateForAllPlayers(int gameId);

    void findGameInProgressStatusAndSendToOnePlayer(User user);

    void drawCardFromDeck(User user, int gameId);

}
