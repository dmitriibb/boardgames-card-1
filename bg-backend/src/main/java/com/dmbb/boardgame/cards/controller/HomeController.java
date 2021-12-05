package com.dmbb.boardgame.cards.controller;

import com.dmbb.boardgame.cards.config.Constants;
import com.dmbb.boardgame.cards.model.dto.GameUpdateDTO;
import com.dmbb.boardgame.cards.model.dto.MessageDTO;
import com.dmbb.boardgame.cards.model.dto.PlayerShortDTO;
import com.dmbb.boardgame.cards.model.entity.CardDescription;
import com.dmbb.boardgame.cards.model.enums.MessageType;
import com.dmbb.boardgame.cards.service.GameService;
import com.dmbb.boardgame.cards.service.MessageService;
import com.dmbb.boardgame.cards.util.MyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final MessageService messageService;

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

    @PutMapping("/message/{player}")
    public void sendMessage(@RequestBody String message, @PathVariable String player) {
        messageService.sendMessageToUser(player, message);
    }

    @PutMapping("/message/game/{player}")
    public void sendGameUpdateMessage(@PathVariable String player) {

        GameUpdateDTO dto = new GameUpdateDTO();
        dto.setTable(new ArrayList<>());
        dto.setMainPlayer(1);
        dto.setActivePlayer(2);
        dto.setMe(new PlayerShortDTO());
        dto.setCardsInDeck(21);

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessageType(MessageType.GAME_UPDATE);
        messageDTO.setPayload(dto);

        messageService.sendMessageToUser(player, Constants.TOPIC_MESSAGES, messageDTO);
    }
}
