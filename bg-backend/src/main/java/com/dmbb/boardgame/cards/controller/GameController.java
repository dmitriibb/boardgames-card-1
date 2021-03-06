package com.dmbb.boardgame.cards.controller;

import com.dmbb.boardgame.cards.model.dto.GameInfoDTO;
import com.dmbb.boardgame.cards.model.dto.GameInfoShortDTO;
import com.dmbb.boardgame.cards.model.dto.NewGameDTO;
import com.dmbb.boardgame.cards.model.entity.User;
import com.dmbb.boardgame.cards.service.GameInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

    private final GameInfoService gameInfoService;

    @GetMapping("/all")
    public List<GameInfoShortDTO> getAll(@AuthenticationPrincipal User user) {
        return gameInfoService.getAllGamesShort(user);
    }

    @GetMapping("/{id}")
    public GameInfoDTO getGameById(@AuthenticationPrincipal User user, @PathVariable int id) {
        return gameInfoService.getGameInfoById(user, id);
    }

    @PostMapping("/new")
    public GameInfoShortDTO createNewGame(@AuthenticationPrincipal User user, @RequestBody NewGameDTO gameDTO) {
        return gameInfoService.createNewGame(user, gameDTO);
    }

    @PutMapping("/start/{gameId}")
    public void startGame(@AuthenticationPrincipal User user, @PathVariable int gameId) {
        //gameInfoService.startGame(user, gameId);
    }

    @PutMapping("/join/{gameId}")
    public void joinGame(@AuthenticationPrincipal User user, @PathVariable int gameId) {
        gameInfoService.joinGame(user, gameId);
    }




}
