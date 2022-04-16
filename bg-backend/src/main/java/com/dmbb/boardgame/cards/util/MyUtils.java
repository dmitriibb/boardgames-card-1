package com.dmbb.boardgame.cards.util;

import com.dmbb.boardgame.cards.model.dto.PlayerShortDTO;
import com.dmbb.boardgame.cards.model.entity.Card;
import com.dmbb.boardgame.cards.model.entity.Image;
import com.dmbb.boardgame.cards.repository.ImageRepository;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.aspectj.util.FileUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class MyUtils {

    public static <T> List<T> getObjectsFromCsvFile(String csvFilePath, Class<T> objectsClass) {
        Gson gson = new Gson();
        return convertCsvToJsonList(csvFilePath).stream()
                .map(str -> gson.fromJson(str, objectsClass))
                .collect(Collectors.toList());
    }

    public static List<String> convertCsvToJsonList(String csvFilePath) {
        List<Map<String, Object>> objectList = new ArrayList<>();

        Resource resource = new ClassPathResource(csvFilePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))){
            String line;
            String[] headers = null;
            while ((line = reader.readLine()) != null) {
                if (headers == null)
                    headers = line.split(",");
                else
                    objectList.add(csvLineToMap(headers, line.split(",")));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        Gson gson = new Gson();
        return objectList.stream()
                .map(gson::toJson)
                .collect(Collectors.toList());
    }

    private static Map<String, Object> csvLineToMap(String[] headers, String[] line) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            if (line.length < i+1) {
                log.info("no value for " + headers[i]);
                continue;
            }
            map.put(headers[i], line[i]);
        }
        return map;
    }

    public static Queue<Card> cardListToQueue(List<Card> cards) {
        return new LinkedList<>(cards);
    }

    public static List<PlayerShortDTO> copyPlayersDTOListExclude(List<PlayerShortDTO> list, int playerExcludeId) {
        return list.stream()
                .filter(p -> p.getId() != playerExcludeId)
                .sorted(Comparator.comparing(PlayerShortDTO::getOrder))
                .collect(Collectors.toList());
    }

    public static List<Image> readImagesFromFile() {
        Resource imagesFolder = new ClassPathResource("data/images_3");

        List<File> imageFiles;

        try {
            imageFiles = Arrays.asList(imagesFolder.getFile().listFiles());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ArrayList<>();
        }

        return imageFiles.stream()
                .map(MyUtils::imageFromFileToEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static Image imageFromFileToEntity(File file) {
        int extDotIndex = file.getName().lastIndexOf(".");
        String name = file.getName().substring(0, extDotIndex);
        String content = null;
        try {
            content = Base64.getEncoder().encodeToString(FileUtil.readAsByteArray(file));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        if (StringUtils.isEmpty(content)) {
            log.error("Empty content for " + file.getName());
            return null;
        }

        Image image = new Image();
        image.setName(name);
        image.setValue(content);
        return image;
    }

}
