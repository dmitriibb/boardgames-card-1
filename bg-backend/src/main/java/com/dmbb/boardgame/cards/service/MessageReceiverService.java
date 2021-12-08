package com.dmbb.boardgame.cards.service;

import com.dmbb.boardgame.cards.model.dto.ClientMessageDTO;
import com.dmbb.boardgame.cards.model.entity.User;

public interface MessageReceiverService {

    void messageFromUser(ClientMessageDTO messageDTO, String auth, String username, Integer gameId);

    void messageFromUserREST(ClientMessageDTO messageDTO, User user, Integer gameId);

}
