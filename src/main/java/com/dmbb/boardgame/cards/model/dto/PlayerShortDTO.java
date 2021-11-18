package com.dmbb.boardgame.cards.model.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PlayerShortDTO {

    @EqualsAndHashCode.Include
    private int id;

    private String name;

    private int order;

    private int coins;

    private int points;

    private int swords;

    private int crosses;

    private int houses;

    private int anchors;

    private int cardNumber;

    private List<CardDTO> cardsHand;

}
