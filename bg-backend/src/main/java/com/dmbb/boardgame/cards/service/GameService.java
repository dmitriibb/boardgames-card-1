package com.dmbb.boardgame.cards.service;

import com.dmbb.boardgame.cards.model.entity.User;

public interface GameService {

    void startGame(User user, int gameId);

}
