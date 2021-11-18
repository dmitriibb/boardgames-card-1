package com.dmbb.boardgame.cards.model.entity;

import com.dmbb.boardgame.cards.model.enums.CardStatus;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "cards")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "card_description_id")
    private CardDescription cardDescription;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    private boolean coin;

    @Enumerated(EnumType.STRING)
    private CardStatus status;

}
