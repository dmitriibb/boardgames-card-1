package com.dmbb.boardgame.cards.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GameUpdateDTO {

    private PlayerFullDTO me;

    private List<PlayerShortDTO> otherPlayers;

    private List<CardDTO> table;

    private int activePlayer;

}
