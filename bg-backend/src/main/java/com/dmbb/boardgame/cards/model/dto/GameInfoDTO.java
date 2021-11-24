package com.dmbb.boardgame.cards.model.dto;

import com.dmbb.boardgame.cards.model.enums.GameStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class GameInfoDTO extends GameInfoShortDTO{

    private PlayerShortDTO admin;

    private Set<PlayerShortDTO> players;

    private GameStatus status;

    private boolean editable;

}
