package com.dmbb.boardgame.cards.service;

import com.dmbb.boardgame.cards.model.dto.CardDescriptionDTO;
import com.dmbb.boardgame.cards.model.entity.Card;
import com.dmbb.boardgame.cards.model.entity.CardDescription;
import com.dmbb.boardgame.cards.model.entity.Game;
import com.dmbb.boardgame.cards.model.entity.Player;

import java.util.List;
import java.util.Map;

public interface CardService {

    List<CardDescriptionDTO> getCardDescriptions();

    CardDescription getCardDescriptionById(int id);

    List<Card> createNewDeckForGame(Game game);

    void save(Card card);

    int getCardInDeckNumber(Game game);

    List<Card> getCardGameTable(Game game);

    Card getCardFromDeck(Game game);

    Card getCardById(int cardId);

    void saveCard(Card card);

    void takeCardsAsCoinsToPlayer(Player player, Game game, int coinsNumber);


}
