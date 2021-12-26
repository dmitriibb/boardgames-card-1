package com.dmbb.boardgame.cards.service;

import com.dmbb.boardgame.cards.model.dto.GameUpdateDTO;
import com.dmbb.boardgame.cards.model.entity.User;

public interface GameService {

    void startGame(User user, int gameId);

    void sendGameUpdateForAllPlayers(int gameId);

    void findGameInProgressStatusAndSendToOnePlayer(User user);

    void drawCardFromDeck(User user, int gameId);

    void sellShip(User user, int gameId, int cardId);

    void destroyShip(User user, int gameId, int cardId);

    void buyPerson(User user, int gameId, int cardId);

    void playerPass(User user, int gameId);

}
