package com.dmbb.boardgame.cards.model.dto;

import com.dmbb.boardgame.cards.model.enums.CardStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardDTO {

    private int id;

    private int descriptionId;

    private CardStatus status;

}
