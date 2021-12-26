package com.dmbb.boardgame.cards.service.impl;

import com.dmbb.boardgame.cards.config.Constants;
import com.dmbb.boardgame.cards.exception.RuleViolationException;
import com.dmbb.boardgame.cards.exception.TmpException;
import com.dmbb.boardgame.cards.model.dto.GameUpdateDTO;
import com.dmbb.boardgame.cards.model.dto.PlayerShortDTO;
import com.dmbb.boardgame.cards.model.dto.ServerMessageDTO;
import com.dmbb.boardgame.cards.model.entity.*;
import com.dmbb.boardgame.cards.model.enums.*;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GameServiceImpl implements GameService {

    private final static int INIT_CARD_AMOUNT = 3;

    private final GameRepository gameRepository;
    private final CardService cardService;
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
        Card card = cardService.getCardFromDeck(game);
        sendGameUpdateToPlayers(game);

        CardDescription cardDescription = cardService.getCardDescriptionById(card.getCardDescriptionId());

        if (cardDescription.getType() == CardType.SHIP) {
            checkFor3SameColorShips(game);
        }

    }

    private void checkFor3SameColorShips(Game game) {
        if (!gameHas3SameColorShips(game))
            return;

        playerService.sendShortMessageToPlayers(game, "3 ships with the same color");
        setNextMainPlayerAndNotify(game);
    }

    private void throwGameTableToGarbage(Game game) {
        List<Card> cardsTable = cardService.getCardGameTable(game);
        cardsTable.forEach(card -> {
            card.setStatus(CardStatus.GARBAGE);
            cardService.save(card);
        });
    }

    @Override
    public void sellShip(User user, int gameId, int cardId) {
        Game game = getGameById(gameId);
        validateActivePlayer(user, game);

        Card card = cardService.getCardById(cardId);
        CardDescription cardDescription = cardService.getCardDescriptionById(card.getCardDescriptionId());
        Player player = playerService.getPlayerByGameAndUser(user, game);

        int coinsToPlayer = cardDescription.getCoins();
        coinsToPlayer += cardService.additionalCoinsForShipColor(player, cardDescription.getColor());
        cardService.takeCardsAsCoinsToPlayer(player, game, coinsToPlayer);

        card.setStatus(CardStatus.GARBAGE);
        cardService.save(card);

        Player mainPlayer = playerService.getPlayerById(game.getMainPlayerId());
        if (!mainPlayer.equals(player)) {
            coinsToPlayer--;
            mainPlayer.setCoins(mainPlayer.getCoins() + 1);
            playerService.savePlayer(mainPlayer);
        }

        player.setCoins(player.getCoins() + coinsToPlayer);
        playerService.savePlayer(player);

        setNextActivePlayerAndNotify(game, player);
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
    public void buyPerson(User user, int gameId, int cardId) {
        Game game = getGameById(gameId);
        validateActivePlayer(user, game);

        Card card = cardService.getCardById(cardId);
        CardDescription cardDescription = cardService.getCardDescriptionById(card.getCardDescriptionId());
        Player player = playerService.getPlayerByGameAndUser(user, game);

        Player mainPlayer = playerService.getPlayerById(game.getMainPlayerId());

        int cost = cardDescription.getCoins();
        if (!mainPlayer.equals(player)) {
            cost++;
        }

        if (player.getCoins() < cost)
            throw new RuleViolationException("You don't have enough money. You have: " + player.getCoins() + ", required: " + cost);

        card.setStatus(CardStatus.PLAYER_TABLE);
        card.setPlayer(player);
        cardService.save(card);

        player.setCoins(player.getCoins() - cost);
        addPersonItemsToPlayer(player, cardDescription);
        playerService.savePlayer(player);

        if (!mainPlayer.equals(player)) {
            mainPlayer.setCoins(mainPlayer.getCoins() + 1);
            playerService.savePlayer(mainPlayer);
        }

        playerService.sendShortMessageToPlayers(game, "Player " + user.getName() + "bought card " + card.getId() + " - " + cardDescription.getName());

        setNextActivePlayerAndNotify(game, player);
    }

    @Override
    public void playerPass(User user, int gameId) {
        Game game = getGameById(gameId);
        validateActivePlayer(user, game);
        Player player = playerService.getPlayerByGameAndUser(user, game);

        setNextActivePlayerAndNotify(game, player);
    }

    private void addPersonItemsToPlayer(Player player, CardDescription cardDescription) {
        player.setPoints(player.getPoints() + cardDescription.getPoints());
        player.setAnchors(player.getAnchors() + cardDescription.getAnchors());
        player.setSwords(player.getSwords() + cardDescription.getSwords());
        player.setCrosses(player.getCrosses() + cardDescription.getCrosses());
        player.setHouses(player.getHouses() + cardDescription.getHouses());
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

            playerService.savePlayer(p);
        }
        gameRepository.save(game);
    }

    private void setNextActivePlayerAndNotify(Game game, Player currentPlayer) {
        Player nextPlayer = playerService.getNextPlayer(game, currentPlayer);

        if (nextPlayer.getId() == game.getMainPlayerId()) {
            setNextMainPlayerAndNotify(game);
        } else {
            game.setActivePlayerId(nextPlayer.getId());
            gameRepository.save(game);
            sendGameUpdateToPlayers(game);
        }
    }

    private void setNextMainPlayerAndNotify(Game game) {
        throwGameTableToGarbage(game);

        Player currentMainPlayer = playerService.getPlayerById(game.getMainPlayerId());
        Player nextMainPlayer = playerService.getNextPlayer(game, currentMainPlayer);
        game.setMainPlayerId(nextMainPlayer.getId());
        game.setActivePlayerId(nextMainPlayer.getId());
        gameRepository.save(game);
        sendGameUpdateToPlayers(game);
    }

    private boolean gameHas3SameColorShips(Game game) {
        List<CardDescription> allShips = cardService.getCardsByGameTypeAndStatus(game, CardType.SHIP, CardStatus.TABLE);
        Map<CardColor, Long> shipsTotal = allShips.stream()
                .collect(Collectors.groupingBy(CardDescription::getColor, Collectors.counting()));

        return shipsTotal.values()
                .stream()
                .anyMatch(val -> val >= 3);
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
            playerService.savePlayer(player);
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
