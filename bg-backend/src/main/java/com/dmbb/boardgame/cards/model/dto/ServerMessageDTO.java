package com.dmbb.boardgame.cards.model.dto;

import com.dmbb.boardgame.cards.model.enums.ServerMessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ServerMessageDTO {

    private ServerMessageType type;

    private Object payload;

}
