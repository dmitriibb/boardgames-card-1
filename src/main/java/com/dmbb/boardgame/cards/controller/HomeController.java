package com.dmbb.boardgame.cards.controller;

import com.dmbb.boardgame.cards.model.entity.CardDescription;
import com.dmbb.boardgame.cards.util.MyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {

    @GetMapping
    public String test() {
        return "Ok";
    }

    @GetMapping("/ships")
    public List<CardDescription> getShipCards() {
        String filePath = "data/ships.csv";
        return MyUtils.getObjectsFromCsvFile(filePath, CardDescription.class);
    }

    @GetMapping("/people")
    public List<CardDescription> getPeopleCards() {
        String filePath = "data/people.csv";
        return MyUtils.getObjectsFromCsvFile(filePath, CardDescription.class);
    }
}
