package com.dmbb.boardgame.cards.model.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GameInfoShortDTO {

    @EqualsAndHashCode.Include
    private int id;

    private String name;

    private boolean secured;

}
