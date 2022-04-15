package com.dmbb.boardgame.cards.service.impl;

import com.dmbb.boardgame.cards.model.dto.ImageDTO;
import com.dmbb.boardgame.cards.model.entity.Image;
import com.dmbb.boardgame.cards.repository.ImageRepository;
import com.dmbb.boardgame.cards.service.ImageService;
import com.dmbb.boardgame.cards.util.MyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    @Transactional
    public void uploadAllImages() {
        imageRepository.deleteAll();
        imageRepository.flush();
        List<Image> images = MyUtils.readImagesFromFile();
        imageRepository.saveAll(images);
    }

    @Override
    public List<ImageDTO> getAllImages() {
        return imageRepository.findAll()
                .stream()
                .map(entity -> {
                    ImageDTO dto = new ImageDTO();
                    dto.setName(entity.getName());
                    dto.setValue(entity.getValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
