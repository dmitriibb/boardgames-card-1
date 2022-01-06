package com.dmbb.boardgame.cards.service;

import com.dmbb.boardgame.cards.model.dto.ImageDTO;
import com.dmbb.boardgame.cards.model.entity.Image;

import java.util.List;

public interface ImageService {

    void uploadAllImages();

    List<ImageDTO> getAllImages();

}
