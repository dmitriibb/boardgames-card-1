package com.dmbb.boardgame.cards.util;

import com.dmbb.boardgame.cards.model.entity.CardDescription;
import com.dmbb.boardgame.cards.model.entity.User;
import com.dmbb.boardgame.cards.model.enums.UserRoles;
import com.dmbb.boardgame.cards.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
@RequiredArgsConstructor
public class Initializer {

    private final UserRepository userRepository;
    private final CardDescriptionRepository cardDescriptionRepository;
    private final PasswordEncoder passwordEncoder;
    private final CardRepository cardRepository;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    @PostConstruct
    public void init() {
        cardRepository.deleteAll();
        playerRepository.deleteAll();
        gameRepository.deleteAll();

        initUsers();
        initCardDescriptions();
    }

    private void initUsers() {
        userRepository.deleteAll();
        userRepository.flush();


        User user = new User();
        user.setEmail("dima@test.com");
        user.setPassword(passwordEncoder.encode("qwerty"));
        user.setName("dima");
        user.setActive(true);

        Set<UserRoles> roles = new HashSet<>();
        roles.add(UserRoles.USER);
        roles.add(UserRoles.ADMIN);
        user.setRoles(roles);
        userRepository.save(user);
    }

    private void initCardDescriptions() {
        cardDescriptionRepository.deleteAll();
        cardDescriptionRepository.flush();
        List<CardDescription> missions = MyUtils.getObjectsFromCsvFile("data/missions.csv", CardDescription.class);
        cardDescriptionRepository.saveAll(missions);

        List<CardDescription> people = MyUtils.getObjectsFromCsvFile("data/people.csv", CardDescription.class);
        cardDescriptionRepository.saveAll(people);

        List<CardDescription> ships = MyUtils.getObjectsFromCsvFile("data/ships.csv", CardDescription.class);
        cardDescriptionRepository.saveAll(ships);

        List<CardDescription> taxes = MyUtils.getObjectsFromCsvFile("data/taxes.csv", CardDescription.class);
        cardDescriptionRepository.saveAll(taxes);
    }

}
