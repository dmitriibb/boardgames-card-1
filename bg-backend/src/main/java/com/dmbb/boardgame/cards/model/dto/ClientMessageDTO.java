package com.dmbb.boardgame.cards.model.dto;

import com.dmbb.boardgame.cards.model.enums.ClientMessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientMessageDTO {
    private ClientMessageType type;
    private String payload;
}
