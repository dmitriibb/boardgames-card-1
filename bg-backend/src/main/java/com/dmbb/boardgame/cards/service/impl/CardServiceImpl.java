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
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardDescriptionRepository cardDescriptionRepository;
    private final CardRepository cardRepository;

    @Override
    public List<CardDescriptionDTO> getCardDescriptions() {
        return cardDescriptionRepository.findAll()
                .stream()
                .map(CardDescription::toDTO)
                .collect(Collectors.toList());
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

    @Override
    public List<Card> getCardGameTable(Game game) {
        return cardRepository.getCardByGameAndStatusOrderByCardOrder(game, CardStatus.TABLE);
    }

    @Override
    public Card getCardFromDeck(Game game) {
        List<Card> cards = cardRepository.getCardByGameAndStatusOrderByCardOrder(game, CardStatus.DECK);
        if (cards.isEmpty())
            throw new RuntimeException("Deck is empty for game: " + game.getId());

        Card card = cards.get(0);
        card.setStatus(CardStatus.TABLE);
        cardRepository.save(card);
        return card;
    }

    private void setCardOrder(List<Card> cards) {
        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).setCardOrder(i);
        }
    }

}
