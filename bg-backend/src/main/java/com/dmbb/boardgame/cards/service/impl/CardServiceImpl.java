package com.dmbb.boardgame.cards.service.impl;

import com.dmbb.boardgame.cards.config.Constants;
import com.dmbb.boardgame.cards.model.dto.CardDescriptionDTO;
import com.dmbb.boardgame.cards.model.entity.Card;
import com.dmbb.boardgame.cards.model.entity.CardDescription;
import com.dmbb.boardgame.cards.model.entity.Game;
import com.dmbb.boardgame.cards.model.entity.Player;
import com.dmbb.boardgame.cards.model.enums.CardColor;
import com.dmbb.boardgame.cards.model.enums.CardStatus;
import com.dmbb.boardgame.cards.model.enums.CardType;
import com.dmbb.boardgame.cards.repository.CardDescriptionRepository;
import com.dmbb.boardgame.cards.repository.CardRepository;
import com.dmbb.boardgame.cards.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public CardDescription getCardDescriptionById(int id) {
        return cardDescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card description is not found for id: " + id));
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

    @Override
    public Card getCardById(int cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("card is not found for id: " + cardId));
    }

    @Override
    public void saveCard(Card card) {
        this.cardRepository.save(card);
    }

    @Override
    public void takeCardsAsCoinsToPlayer(Player player, Game game, int coinsNumber) {
        Pageable pageable = PageRequest.of(0, coinsNumber, Sort.by("cardOrder"));
        List<Card> cards = cardRepository.getCardByGameAndStatus(game, CardStatus.DECK, pageable);

        if (cards.size() < coinsNumber)
            throw new RuntimeException("Not enough cards in the deck. Current deck size is " + cards.size() + " cards");

        cards.forEach(card -> {
            card.setStatus(CardStatus.PLAYER_HAND);
            card.setPlayer(player);
            cardRepository.save(card);
        });
    }

    @Override
    public List<CardDescription> getCardsByGameTypeAndStatus(Game game, CardType type, CardStatus status) {
        return cardDescriptionRepository.getAllByGameAndTypeAndCardStatus(game, type, status);
    }

    @Override
    public int additionalCoinsForShipColor(Player player, CardColor cardColor) {
        return cardDescriptionRepository.countByPlayerAndStatusAndNameAndTypeAndColor(player, CardStatus.PLAYER_TABLE,
                Constants.CARD_PERSON_TRADER, CardType.PERSON, cardColor);
    }

    private void setCardOrder(List<Card> cards) {
        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).setCardOrder(i);
        }
    }

}
