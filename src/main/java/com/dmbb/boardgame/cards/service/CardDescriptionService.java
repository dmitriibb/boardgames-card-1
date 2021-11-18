package com.dmbb.boardgame.cards.service;

import com.dmbb.boardgame.cards.model.dto.CardDescriptionDTO;
import com.dmbb.boardgame.cards.model.entity.CardDescription;

import java.util.List;

public interface CardDescriptionService {

    List<CardDescription> getAll();

    List<CardDescriptionDTO> getFullDesk();


}
