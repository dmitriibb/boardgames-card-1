package com.dmbb.boardgame.cards.service.impl;

import com.dmbb.boardgame.cards.config.Constants;
import com.dmbb.boardgame.cards.exception.RuleViolationException;
import com.dmbb.boardgame.cards.exception.TmpException;
import com.dmbb.boardgame.cards.model.dto.GameUpdateDTO;
import com.dmbb.boardgame.cards.model.dto.PlayerShortDTO;
import com.dmbb.boardgame.cards.model.dto.ServerMessageDTO;
import com.dmbb.boardgame.cards.model.entity.*;
import com.dmbb.boardgame.cards.model.enums.CardStatus;
import com.dmbb.boardgame.cards.model.enums.GameStatus;
import com.dmbb.boardgame.cards.model.enums.ServerMessageType;
import com.dmbb.boardgame.cards.repository.GameRepository;
import com.dmbb.boardgame.cards.repository.PlayerRepository;
import com.dmbb.boardgame.cards.service.CardService;
import com.dmbb.boardgame.cards.service.GameService;
import com.dmbb.boardgame.cards.service.PlayerService;
import com.dmbb.boardgame.cards.util.MyUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GameServiceImpl implements GameService {

    private final static int INIT_CARD_AMOUNT = 3;

    private final SimpMessageSendingOperations messagingTemplate;
    private final GameRepository gameRepository;
    private final CardService cardService;
    private final PlayerRepository playerRepository;
    private final PlayerService playerService;

    @Override
    @Transactional
    public void startGame(User user, int gameId) {
        Game game = getGameById(gameId);

        validateGameBeforeStart(user, game);

        game.setStatus(GameStatus.IN_PROGRESS);
        gameRepository.save(game);
        Queue<Card> deck = MyUtils.cardListToQueue(cardService.createNewDeckForGame(game));

        distributeFirstCardsForPlayers(game, deck);
        setPlayersOrder(game);
        sendGameUpdateToPlayers(game);
    }

    @Override
    public void sendGameUpdateForAllPlayers(int gameId) {
        Game game = getGameById(gameId);
        sendGameUpdateToPlayers(game);
    }

    @Override
    @Transactional
    public void findGameInProgressStatusAndSendToOnePlayer(User user) {
        List<Game> userGamesInProgress = gameRepository.getGamesWithStatusForUser(GameStatus.IN_PROGRESS, user);
        if (userGamesInProgress.isEmpty())
            return;

        Game game = userGamesInProgress.get(0);
        GameUpdateDTO gameUpdateDTO = gameEntityToUpdateDTO(game);

        PlayerShortDTO me = gameUpdateDTO.getOtherPlayers()
                .stream()
                .filter(p -> p.getUsername().equals(user.getUsername()))
                .findFirst().get();

        gameUpdateDTO.setMe(me);
        gameUpdateDTO.setOtherPlayers(MyUtils.copyPlayersDTOListExclude(gameUpdateDTO.getOtherPlayers(), me.getId()));
        ServerMessageDTO messageDTO = new ServerMessageDTO(ServerMessageType.GAME_UPDATE, gameUpdateDTO);
        playerService.sendMessageToUser(user.getUsername(), Constants.TOPIC_MESSAGES, messageDTO);
    }

    @Override
    public void drawCardFromDeck(User user, int gameId) {
        log.info(user.getName() + " is drawing card from deck for game id: " + gameId);
        Game game = getGameById(gameId);
        validateActivePlayer(user, game);
        cardService.getCardFromDeck(game);
        sendGameUpdateToPlayers(game);

    }

    @Override
    public void sellShip(User user, int gameId, int cardId) {
        Game game = getGameById(gameId);
        validateActivePlayer(user, game);

        Card card = cardService.getCardById(cardId);
        CardDescription cardDescription = cardService.getCardDescriptionById(card.getCardDescriptionId());
        Player player = playerService.getPlayerByGameAndUser(user, game);

        cardService.takeCardsAsCoinsToPlayer(player, game, cardDescription.getCoins());

        card.setStatus(CardStatus.GARBAGE);
        cardService.save(card);

        player.setCoins(player.getCoins() + cardDescription.getCoins());
        playerService.savePlayer(player);

        setNextActivePlayerAndNotify(game, player);
        //sendGameUpdateToPlayers(game.getId());
    }

    @Override
    public void destroyShip(User user, int gameId, int cardId) {
        Game game = getGameById(gameId);
        validateActivePlayer(user, game);

        Card card = cardService.getCardById(cardId);
        CardDescription cardDescription = cardService.getCardDescriptionById(card.getCardDescriptionId());
        Player player = playerService.getPlayerByGameAndUser(user, game);

        int shipSwords = cardDescription.getSwords();
        int playerSwords = player.getSwords();

        if (playerSwords < shipSwords)
            throw new RuleViolationException("You don't have enough swords!\nYou have: " + playerSwords + "\nRequired: " + shipSwords);

        card.setStatus(CardStatus.GARBAGE);
        cardService.save(card);

        sendGameUpdateToPlayers(game.getId());
    }

    @Override
    public void playerPass(User user, int gameId) {
        Game game = getGameById(gameId);
        validateActivePlayer(user, game);
        Player player = playerService.getPlayerByGameAndUser(user, game);

        setNextActivePlayerAndNotify(game, player);
    }

    private void sendGameUpdateToPlayers(int gameId) {
        Game game = getGameById(gameId);
        sendGameUpdateToPlayers(game);
    }

    private void sendGameUpdateToPlayers(Game game) {
        GameUpdateDTO gameDTO = gameEntityToUpdateDTO(game);
        playerService.notifyPlayers(gameDTO);
    }

    private GameUpdateDTO gameEntityToUpdateDTO(Game game) {
        int mainPlayerId = game.getMainPlayerId();
        int activePlayerId = game.getActivePlayerId();
        GameUpdateDTO gameDTO = new GameUpdateDTO();
        gameDTO.setId(game.getId());
        gameDTO.setMainPlayerId(mainPlayerId);
        gameDTO.setActivePlayerId(activePlayerId);
        gameDTO.setTable(cardService.getCardGameTable(game)
                .stream()
                .map(Card::toDTO)
                .collect(Collectors.toList()));
        gameDTO.setCardsInDeck(cardService.getCardInDeckNumber(game));
        gameDTO.setOtherPlayers(playerService.getPlayersByGame(game)
                .stream()
                .map(p -> {
                    PlayerShortDTO pDTO = p.toDTO();
                    pDTO.setMain(pDTO.getId() == mainPlayerId);
                    pDTO.setActive(pDTO.getId() == activePlayerId);
                    return pDTO;
                })
                .collect(Collectors.toList()));
        return gameDTO;
    }

    private void setPlayersOrder(Game game) {
        List<Player> players = new ArrayList<>(game.getPlayers());
        players.sort(Comparator.comparing(Player::getId));
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            p.setPlayerOrder(i);

            if (i == 0) {
                game.setMainPlayerId(p.getId());
                game.setActivePlayerId(p.getId());
            }

            playerRepository.save(p);
        }
        gameRepository.save(game);
    }

    private void setNextActivePlayerAndNotify(Game game, Player currentPlayer) {
        Player nextPlayer = playerService.getNextPlayer(game, currentPlayer);
        if (nextPlayer.getId() == game.getMainPlayerId()) {
            nextPlayer = playerService.getNextPlayer(game, nextPlayer);
            game.setMainPlayerId(nextPlayer.getId());
        }
        game.setActivePlayerId(nextPlayer.getId());
        gameRepository.save(game);

        sendGameUpdateToPlayers(game);
    }

    private void distributeFirstCardsForPlayers(Game game, Queue<Card> deck) {
        game.getPlayers().forEach(player -> {
            for (int i = 0; i < INIT_CARD_AMOUNT; i++) {
                Card card = deck.poll();
                card.setPlayer(player);
                card.setStatus(CardStatus.PLAYER_HAND);
                card.setCoin(true);
                cardService.save(card);
            }
            player.setCoins(INIT_CARD_AMOUNT);
            playerRepository.save(player);
        });
    }

    private void validateGameBeforeStart(User admin, Game game) {
        if (!game.getAdmin().equals(admin))
            throw new TmpException("You are not allowed to start this game");

        if (game.getPlayers().size() < 2)
            throw new TmpException("Not enough players");

        if (game.getStatus() != GameStatus.AWAITING)
            throw new TmpException("Game can be started only in status " + GameStatus.AWAITING);
    }

    private void validateActivePlayer(User user, Game game) {
        Player player = playerService.getPlayerById(game.getActivePlayerId());

        if (!player.getUser().equals(user))
            throw new RuleViolationException("You are not the active player");
    }

    private Game getGameById(int gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new TmpException("Game is not found for id: " + gameId));
    }

}
