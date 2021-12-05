package com.dmbb.boardgame.cards.service;

public interface MessageService {

    void sendMessageToUser(String user, String message);

    void sendMessageToUser(String username, String destination, Object payload);

}
