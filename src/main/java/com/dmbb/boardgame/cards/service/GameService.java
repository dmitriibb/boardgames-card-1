package com.dmbb.boardgame.cards.service;

import com.dmbb.boardgame.cards.model.dto.GameDTO;
import com.dmbb.boardgame.cards.model.entity.User;

import java.util.List;

public interface GameService {

    GameDTO createNewGame(User user, GameDTO newGameDTO);

    List<GameDTO> getAllGames();

    List<GameDTO> getGamesByAdmin(User user);

}
