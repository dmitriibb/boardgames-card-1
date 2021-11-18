package com.dmbb.boardgame.cards.util;

import com.dmbb.boardgame.cards.model.entity.Card;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
            map.put(headers[i], line[i]);
        }
        return map;
    }

    public static Queue<Card> cardListToQueue(List<Card> cards) {
        return new LinkedList<>(cards);
    }

}
