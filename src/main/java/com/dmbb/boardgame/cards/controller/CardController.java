package com.dmbb.boardgame.cards.controller;

import com.dmbb.boardgame.cards.model.dto.CardDescriptionDTO;
import com.dmbb.boardgame.cards.service.CardDescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
public class CardController {

    private final CardDescriptionService cardDescriptionService;

    @GetMapping("/desk")
    public List<CardDescriptionDTO> getFullDesk() {
        return cardDescriptionService.getFullDesk();
    }


}
