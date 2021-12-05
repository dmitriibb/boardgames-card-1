package com.dmbb.boardgame.cards.service;

import com.dmbb.boardgame.cards.model.dto.GameInfoDTO;
import com.dmbb.boardgame.cards.model.dto.GameInfoShortDTO;
import com.dmbb.boardgame.cards.model.dto.NewGameDTO;
import com.dmbb.boardgame.cards.model.entity.User;

import java.util.List;

public interface GameInfoService {

    GameInfoShortDTO createNewGame(User user, NewGameDTO gameDTO);

    void joinGame(User user, int gameId);

    List<GameInfoShortDTO> getAllGamesShort(User user);

    GameInfoDTO getGameInfoById(User user, int id);

    List<GameInfoDTO> getGamesByAdmin(User user);

}
