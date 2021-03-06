package com.dmbb.boardgame.cards.service;

import com.dmbb.boardgame.cards.model.dto.CardDescriptionDTO;
import com.dmbb.boardgame.cards.model.entity.Card;
import com.dmbb.boardgame.cards.model.entity.CardDescription;
import com.dmbb.boardgame.cards.model.entity.Game;
import com.dmbb.boardgame.cards.model.entity.Player;
import com.dmbb.boardgame.cards.model.enums.CardColor;
import com.dmbb.boardgame.cards.model.enums.CardStatus;
import com.dmbb.boardgame.cards.model.enums.CardType;

import java.util.List;
import java.util.Map;

public interface CardService {

    List<CardDescriptionDTO> getCardDescriptions();

    CardDescription getCardDescriptionById(int id);

    List<Card> createNewDeckForGame(Game game);

    void save(Card card);

    int getCardInDeckNumber(Game game);

    List<Card> getCardsOnTable(Game game);

    Card getLastCardOnTable(Game game);

    Card getCardFromDeck(Game game);

    Card getCardById(int cardId);

    void takeCardsAsCoinsToPlayer(Player player, Game game, int coinsNumber);

    List<CardDescription> getCardsByGameTypeAndStatus(Game game, CardType type, CardStatus status);

    int tradersNumberOfPlayer(Player player, CardColor cardColor);

}
