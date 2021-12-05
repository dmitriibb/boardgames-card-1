package com.dmbb.boardgame.cards.model.dto;

import com.dmbb.boardgame.cards.model.enums.MessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO {

    private MessageType messageType;

    private Object payload;

}
