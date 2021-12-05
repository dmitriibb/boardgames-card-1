package com.dmbb.boardgame.cards.service;

import com.dmbb.boardgame.cards.model.dto.CardDescriptionDTO;
import com.dmbb.boardgame.cards.model.entity.Card;
import com.dmbb.boardgame.cards.model.entity.CardDescription;
import com.dmbb.boardgame.cards.model.entity.Game;

import java.util.List;

public interface CardService {

    List<CardDescription> getAll();

    List<Card> createNewDeckForGame(Game game);

    void save(Card card);

    int getCardInDeckNumber(Game game);


}
