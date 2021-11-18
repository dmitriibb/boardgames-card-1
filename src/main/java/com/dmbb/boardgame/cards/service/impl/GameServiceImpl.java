package com.dmbb.boardgame.cards.service.impl;

import com.dmbb.boardgame.cards.exception.TmpException;
import com.dmbb.boardgame.cards.model.dto.GameDTO;
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
    public GameDTO createNewGame(User user, GameDTO newGameDTO) {
        Game gameEntity = new Game();
        gameEntity.setAdmin(user);
        gameEntity.setName(newGameDTO.getName());
        gameEntity.setStatus(GameStatus.AWAITING);
        Game savedGame = gameRepository.save(gameEntity);

        Player player = new Player();
        player.setGame(savedGame);
        player.setUser(user);
        player.setOrder(0);
        player.setStatus(PlayerStatus.WAITING);
        playerRepository.save(player);

        return gameEntityToDTO(savedGame);
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
    public List<GameDTO> getAllGames() {
        return gameRepository.findAll()
                .stream()
                .map(this::gameEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<GameDTO> getGamesByAdmin(User user) {
        return gameRepository.findAllByAdmin(user)
                .stream()
                .map(this::gameEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void startGame(User admin, int gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new TmpException("Game is nor found"));

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

    private GameDTO gameEntityToDTO(Game entity) {
        GameDTO dto = new GameDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setStatus(entity.getStatus());
        dto.setAdmin(userEntityToDTO(entity.getAdmin()));
        dto.setPlayers(entity.getPlayers()
                .stream()
                .map(this::playerEntityToDTO)
                .collect(Collectors.toSet()));

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
