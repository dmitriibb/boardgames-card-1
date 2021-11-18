package com.dmbb.boardgame.cards.service;

import com.dmbb.boardgame.cards.model.entity.Game;
import com.dmbb.boardgame.cards.model.entity.Player;

import java.util.Collection;

public interface PlayerService {

    void notifyPlayers(Game game);

}
