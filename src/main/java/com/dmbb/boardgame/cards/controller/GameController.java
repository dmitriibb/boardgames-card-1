package com.dmbb.boardgame.cards.controller;

import com.dmbb.boardgame.cards.model.dto.GameDTO;
import com.dmbb.boardgame.cards.model.entity.User;
import com.dmbb.boardgame.cards.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping("/all")
    public List<GameDTO> getAll() {
        return gameService.getAllGames();
    }

    @PostMapping("/new")
    public GameDTO createNewGame(@AuthenticationPrincipal User user, @RequestBody GameDTO dto) {
        return gameService.createNewGame(user, dto);
    }



}
