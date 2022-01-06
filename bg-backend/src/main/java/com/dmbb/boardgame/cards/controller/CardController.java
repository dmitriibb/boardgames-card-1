package com.dmbb.boardgame.cards.controller;

import com.dmbb.boardgame.cards.model.dto.CardDescriptionDTO;
import com.dmbb.boardgame.cards.model.dto.ImageDTO;
import com.dmbb.boardgame.cards.service.CardService;
import com.dmbb.boardgame.cards.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    private final ImageService imageService;

    @GetMapping("/descriptions")
    public List<CardDescriptionDTO> getCardDescriptionsMap() {
        return cardService.getCardDescriptions();
    }

    @GetMapping("/images")
    public List<ImageDTO> getCardDescriptionImages() {
        return imageService.getAllImages();
    }

}
