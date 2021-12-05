package com.dmbb.boardgame.cards.service.impl;

import com.dmbb.boardgame.cards.model.dto.CardDescriptionDTO;
import com.dmbb.boardgame.cards.model.entity.Card;
import com.dmbb.boardgame.cards.model.entity.CardDescription;
import com.dmbb.boardgame.cards.model.entity.Game;
import com.dmbb.boardgame.cards.model.enums.CardStatus;
import com.dmbb.boardgame.cards.repository.CardDescriptionRepository;
import com.dmbb.boardgame.cards.repository.CardRepository;
import com.dmbb.boardgame.cards.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardDescriptionRepository cardDescriptionRepository;
    private final CardRepository cardRepository;

    @Override
    public List<CardDescription> getAll() {
        return cardDescriptionRepository.findAll();
    }

    @Override
    public List<Card> createNewDeckForGame(Game game) {
        List<Card> cards = new ArrayList<>();

        cardDescriptionRepository.findAll().forEach(cd -> {
            for (int i = 0; i < cd.getAmount(); i++) {
                Card card = new Card();
                card.setGame(game);
                card.setCardDescriptionId(cd.getId());
                card.setCoin(false);
                card.setStatus(CardStatus.DECK);
                cards.add(card);
            }
        });

        Collections.shuffle(cards);
        setCardOrder(cards);

        cardRepository.saveAll(cards);
        return cardRepository.findAllByGameOrderByCardOrder(game);
    }

    @Override
    public void save(Card card) {
        cardRepository.save(card);
    }

    @Override
    public int getCardInDeckNumber(Game game) {
        return cardRepository.countByGameAndStatus(game, CardStatus.DECK);
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

    private void setCardOrder(List<Card> cards) {
        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).setCardOrder(i);
        }
    }

}
