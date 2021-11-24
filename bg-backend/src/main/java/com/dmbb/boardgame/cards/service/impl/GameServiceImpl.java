package com.dmbb.boardgame.cards.service.impl;

import com.dmbb.boardgame.cards.exception.TmpException;
import com.dmbb.boardgame.cards.model.dto.GameInfoDTO;
import com.dmbb.boardgame.cards.model.dto.GameInfoShortDTO;
import com.dmbb.boardgame.cards.model.dto.NewGameDTO;
import com.dmbb.boardgame.cards.model.dto.PlayerShortDTO;
import com.dmbb.boardgame.cards.model.entity.Card;
import com.dmbb.boardgame.cards.model.entity.Game;
import com.dmbb.boardgame.cards.model.entity.Player;
import com.dmbb.boardgame.cards.model.entity.User;
import com.dmbb.boardgame.cards.model.enums.CardStatus;
import com.dmbb.boardgame.cards.model.enums.GameStatus;
import com.dmbb.boardgame.cards.model.enums.PlayerStatus;
import com.dmbb.boardgame.cards.repository.GameRepository;
import com.dmbb.boardgame.cards.repository.PlayerRepository;
import com.dmbb.boardgame.cards.service.CardService;
import com.dmbb.boardgame.cards.service.GameService;
import com.dmbb.boardgame.cards.service.PlayerService;
import com.dmbb.boardgame.cards.util.MyUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final static int INIT_CARD_AMOUNT = 3;

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final CardService cardService;
    private final PlayerService playerService;

    @Override
    public GameInfoShortDTO createNewGame(User user, NewGameDTO gameDTO) {
        Game gameEntity = new Game();
        gameEntity.setAdmin(user);
        gameEntity.setName(gameDTO.getName());
        gameEntity.setStatus(GameStatus.AWAITING);
        gameEntity.setPassword(gameDTO.getPassword());
        Game savedGame = gameRepository.save(gameEntity);

        Player player = new Player();
        player.setGame(savedGame);
        player.setUser(user);
        player.setOrder(0);
        player.setStatus(PlayerStatus.WAITING);
        playerRepository.save(player);

        return gameEntityToShortDTO(savedGame);
    }

    @Override
    public void joinGame(User user, int gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new TmpException("Game is nor found"));

        Set<User> players = game.getPlayers()
                .stream()
                .map(Player::getUser)
                .collect(Collectors.toSet());

        if (players.contains(user))
            throw new TmpException("You have already been joined");

        Player player = new Player();
        player.setGame(game);
        player.setUser(user);
        player.setOrder(players.size());
        player.setStatus(PlayerStatus.WAITING);
        playerRepository.save(player);
    }

    @Override
    public List<GameInfoShortDTO> getAllGamesShort(User user) {
        return gameRepository.findAll()
                .stream()
                .map(game -> gameEntityToShortDTO(game))
                .collect(Collectors.toList());
    }

    @Override
    public GameInfoDTO getGameInfoById(User user, int id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new TmpException("Game is not found"));

        return gameEntityToDTO(game, user);
    }

    @Override
    public List<GameInfoDTO> getGamesByAdmin(User user) {
        return gameRepository.findAllByAdmin(user)
                .stream()
                .map(game -> gameEntityToDTO(game, user))
                .collect(Collectors.toList());
    }

    @Override
    public void startGame(User admin, int gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new TmpException("Game is not found"));

        validateGameBeforeStart(admin, game);
        game.setStatus(GameStatus.IN_PROCESS);
        gameRepository.save(game);
        Queue<Card> desk =MyUtils.cardListToQueue(cardService.createNewDeskForGame(game));

        distributeFirstCardsForPlayers(game, desk);

    }

    private void distributeFirstCardsForPlayers(Game game, Queue<Card> desk) {
        game.getPlayers().forEach(player -> {
            for (int i = 0; i < INIT_CARD_AMOUNT; i++) {
                Card card = desk.poll();
                card.setPlayer(player);
                card.setStatus(CardStatus.PLAYER_HAND);
                cardService.save(card);
                player.getCards().add(card);
            }
        });
    }

    private GameInfoShortDTO gameEntityToShortDTO(Game entity) {
        GameInfoShortDTO dto = new GameInfoShortDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSecured(StringUtils.isNotEmpty(entity.getPassword()));
        return dto;
    }

    private GameInfoDTO gameEntityToDTO(Game entity, User currentUser) {
        GameInfoDTO dto = new GameInfoDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setStatus(entity.getStatus());
        dto.setAdmin(userEntityToDTO(entity.getAdmin()));
        dto.setPlayers(entity.getPlayers()
                .stream()
                .map(this::playerEntityToDTO)
                .collect(Collectors.toSet()));

        dto.setEditable(entity.getAdmin().equals(currentUser));

        return dto;
    }

    private PlayerShortDTO playerEntityToDTO(Player player) {
        PlayerShortDTO dto = new PlayerShortDTO();
        dto.setId(player.getId());
        dto.setName(player.getUser().getName());
        return dto;
    }

    private PlayerShortDTO userEntityToDTO(User user) {
        PlayerShortDTO dto = new PlayerShortDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        return dto;
    }

    private void validateGameBeforeStart(User admin, Game game) {
        if (!game.getAdmin().equals(admin))
            throw new TmpException("You are not allowed to start this game");

        if (game.getPlayers().size() < 2)
            throw new TmpException("Not enough players");

        if (game.getStatus() != GameStatus.AWAITING)
            throw new TmpException("Game can be started only in status " + GameStatus.AWAITING);
    }


}
