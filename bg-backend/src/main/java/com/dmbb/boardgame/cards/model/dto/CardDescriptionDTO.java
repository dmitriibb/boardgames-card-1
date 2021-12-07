package com.dmbb.boardgame.cards.model.dto;

import com.dmbb.boardgame.cards.model.enums.CardColor;
import com.dmbb.boardgame.cards.model.enums.CardType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardDescriptionDTO {

    private int id;

    private String name;

    private CardType type;

    private int points;

    private int coins;

    private CardColor color;

    private int houses;

    private int crosses;

    private int anchors;

    private int swords;

}
