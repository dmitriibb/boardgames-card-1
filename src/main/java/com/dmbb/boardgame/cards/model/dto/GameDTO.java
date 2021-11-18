package com.dmbb.boardgame.cards.model.dto;

import com.dmbb.boardgame.cards.model.enums.GameStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GameDTO {

    @EqualsAndHashCode.Include
    private int id;

    private String name;

    private PlayerDTO admin;

    private Set<PlayerDTO> players;

    private GameStatus status;

}
