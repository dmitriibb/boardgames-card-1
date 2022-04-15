package com.dmbb.boardgame.cards.controller;

import com.dmbb.boardgame.cards.config.Constants;
import com.dmbb.boardgame.cards.model.dto.ImageDTO;
import com.dmbb.boardgame.cards.model.dto.ServerMessageDTO;
import com.dmbb.boardgame.cards.model.entity.CardDescription;
import com.dmbb.boardgame.cards.model.enums.ServerMessageType;
import com.dmbb.boardgame.cards.service.GameService;
import com.dmbb.boardgame.cards.service.ImageService;
import com.dmbb.boardgame.cards.service.PlayerService;
import com.dmbb.boardgame.cards.util.MyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final PlayerService playerService;
    private final GameService gameService;
    private final ImageService imageService;

    @GetMapping
    public String test() {
        return "Ok";
    }

    @GetMapping("/ships")
    public List<CardDescription> getShipCards() {
        String filePath = "data/ships.csv";
        return MyUtils.getObjectsFromCsvFile(filePath, CardDescription.class);
    }

    @GetMapping("/ships/2/dadada")
    public List<CardDescription> getShipCards2() {
        String filePath = "data/ships.csv";
        return MyUtils.getObjectsFromCsvFile(filePath, CardDescription.class);
    }

    @GetMapping("/people")
    public List<CardDescription> getPeopleCards() {
        String filePath = "data/people.csv";
        return MyUtils.getObjectsFromCsvFile(filePath, CardDescription.class);
    }

    @PutMapping("/game-update/{gameId}")
    public void sendGameUpdate(@PathVariable int gameId) {
        gameService.sendGameUpdateForAllPlayers(gameId);
    }

    @PutMapping("/short-message/{username}")
    public void sendShortMessageToUser(@PathVariable String username, @RequestBody String message) {
        ServerMessageDTO dto = new ServerMessageDTO(ServerMessageType.SHORT_MESSAGE, message);
        playerService.sendMessageToUser(username, Constants.TOPIC_MESSAGES, dto);
    }

    @GetMapping("/upload-images")
    public void uploadImages() {
        imageService.uploadAllImages();
    }

    @GetMapping("/images")
    public List<ImageDTO> getImages() {
        return imageService.getAllImages().subList(0, 2);
    }

}
