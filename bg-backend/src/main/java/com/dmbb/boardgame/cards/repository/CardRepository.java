package com.dmbb.boardgame.cards.repository;

import com.dmbb.boardgame.cards.model.entity.Card;
import com.dmbb.boardgame.cards.model.entity.CardDescription;
import com.dmbb.boardgame.cards.model.entity.Game;
import com.dmbb.boardgame.cards.model.enums.CardStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {

    List<Card> findAllByGameOrderByCardOrder(Game game);

    int countByGameAndStatus(Game game, CardStatus status);

    List<Card> getCardByGameAndStatusOrderByCardOrder(Game game, CardStatus status);

    List<Card> getCardByGameAndStatus(Game game, CardStatus status, Pageable pageable);

    Card findFirstByGameAndStatusOrderByCardOrderDesc(Game game, CardStatus status);

}
