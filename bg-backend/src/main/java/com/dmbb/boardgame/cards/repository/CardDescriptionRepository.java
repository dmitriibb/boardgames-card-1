package com.dmbb.boardgame.cards.repository;

import com.dmbb.boardgame.cards.model.entity.CardDescription;
import com.dmbb.boardgame.cards.model.entity.Game;
import com.dmbb.boardgame.cards.model.entity.Player;
import com.dmbb.boardgame.cards.model.enums.CardColor;
import com.dmbb.boardgame.cards.model.enums.CardStatus;
import com.dmbb.boardgame.cards.model.enums.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardDescriptionRepository extends JpaRepository<CardDescription, Integer> {

    @Query("select cd from CardDescription cd join Card c on c.cardDescriptionId = cd.id " +
            "where c.game = :game and c.status = :status and cd.type = :type")
    List<CardDescription> getAllByGameAndTypeAndCardStatus(Game game, CardType type, CardStatus status);

    @Query("select count(cd) from CardDescription cd join Card c on c.cardDescriptionId = cd.id " +
            "where c.player = :player " +
            "and c.status = :cardStatus " +
            "and cd.name = :cardDescriptionName " +
            "and cd.type = :cardType " +
            "and cd.color = :color")
    int countByPlayerAndStatusAndNameAndTypeAndColor(Player player, CardStatus cardStatus,
                                              String cardDescriptionName, CardType cardType,
                                              CardColor color);

}
