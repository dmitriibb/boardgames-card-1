package com.dmbb.boardgame.cards.exception;

public class BGNotAuthorizedException extends RuntimeException {

    public BGNotAuthorizedException(String message) {
        super(message);
    }
}
