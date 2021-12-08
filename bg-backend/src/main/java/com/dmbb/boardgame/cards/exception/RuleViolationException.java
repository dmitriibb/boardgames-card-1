package com.dmbb.boardgame.cards.exception;

public class RuleViolationException extends ClientErrorException {

    public RuleViolationException(String message) {
        super(message);
    }

}
