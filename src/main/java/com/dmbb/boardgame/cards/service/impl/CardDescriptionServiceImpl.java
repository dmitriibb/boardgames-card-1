package com.dmbb.boardgame.cards.service.impl;

import com.dmbb.boardgame.cards.model.dto.CardDescriptionDTO;
import com.dmbb.boardgame.cards.model.entity.CardDescription;
import com.dmbb.boardgame.cards.repository.CardDescriptionRepository;
import com.dmbb.boardgame.cards.service.CardDescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardDescriptionServiceImpl implements CardDescriptionService {

    private final CardDescriptionRepository cardDescriptionRepository;

    @Override
    public List<CardDescription> getAll() {
        return cardDescriptionRepository.findAll();
    }

    @Override
    public List<CardDescriptionDTO> getFullDesk() {
        List<CardDescriptionDTO> desk = new ArrayList<>();

        getAll().forEach(card -> {
            for (int i = 0; i < card.getAmount(); i++) {
                desk.add(entityToDTO(card));
            }
        });

        return desk;
    }

    private CardDescriptionDTO entityToDTO(CardDescription entity) {
        CardDescriptionDTO dto = new CardDescriptionDTO();
        dto.setType(entity.getType());
        dto.setName(entity.getName());
        dto.setCoins(entity.getCoins());
        dto.setPoints(entity.getPoints());
        dto.setColor(entity.getColor());
        dto.setAnchors(entity.getAnchors());
        dto.setCrosses(entity.getCrosses());
        dto.setHouses(entity.getHouses());
        dto.setSwords(entity.getSwords());
        return dto;
    }

}
