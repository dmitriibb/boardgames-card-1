package com.dmbb.boardgame.cards.util;

import com.dmbb.boardgame.cards.model.dto.GameInfoShortDTO;
import com.dmbb.boardgame.cards.model.dto.NewGameDTO;
import com.dmbb.boardgame.cards.model.entity.CardDescription;
import com.dmbb.boardgame.cards.model.entity.Game;
import com.dmbb.boardgame.cards.model.entity.User;
import com.dmbb.boardgame.cards.model.enums.UserRoles;
import com.dmbb.boardgame.cards.repository.*;
import com.dmbb.boardgame.cards.service.GameInfoService;
import com.dmbb.boardgame.cards.service.GameService;
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
    private final GameInfoService gameInfoService;
    private final GameService gameService;

    @PostConstruct
    public void init() {
        cardRepository.deleteAll();
        playerRepository.deleteAll();
        gameRepository.deleteAll();

        initUsers();
        initCardDescriptions();
        initGames();
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

        User user2 = new User();
        user2.setEmail("john@test.com");
        user2.setPassword(passwordEncoder.encode("qwerty2"));
        user2.setName("john");
        user2.setActive(true);

        Set<UserRoles> roles2 = new HashSet<>();
        roles2.add(UserRoles.USER);
        roles2.add(UserRoles.ADMIN);
        user2.setRoles(roles2);
        userRepository.save(user2);
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

    private void initGames() {
        gameRepository.deleteAll();
        gameRepository.flush();

        User admin = userRepository.getByEmail("dima@test.com");
        NewGameDTO newGameDTO = new NewGameDTO();
        newGameDTO.setName("test game 1");
        GameInfoShortDTO gameInfoShortDTO = gameInfoService.createNewGame(admin, newGameDTO);

        User user2 = userRepository.getByEmail("john@test.com");
        gameInfoService.joinGame(user2, gameInfoShortDTO.getId());

        gameService.startGame(admin, gameInfoShortDTO.getId());
    }

}
