package com.dmbb.boardgame.cards.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private boolean main;

    private boolean active;

    private int order;

    private int coins;

    private int points;

    private int swords;

    private int crosses;

    private int houses;

    private int anchors;

    private List<CardDTO> cards;

    @JsonIgnore
    private String username;

}
