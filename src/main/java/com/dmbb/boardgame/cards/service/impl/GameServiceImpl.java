package com.dmbb.boardgame.cards.service.impl;

import com.dmbb.boardgame.cards.model.dto.GameDTO;
import com.dmbb.boardgame.cards.model.dto.PlayerDTO;
import com.dmbb.boardgame.cards.model.dto.UserDTO;
import com.dmbb.boardgame.cards.model.entity.Game;
import com.dmbb.boardgame.cards.model.entity.User;
import com.dmbb.boardgame.cards.model.enums.GameStatus;
import com.dmbb.boardgame.cards.repository.GameRepository;
import com.dmbb.boardgame.cards.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    @Override
    public GameDTO createNewGame(User user, GameDTO newGameDTO) {
        Game gameEntity = new Game();
        gameEntity.setAdmin(user);
        gameEntity.setName(newGameDTO.getName());
        gameEntity.setStatus(GameStatus.AWAITING);
        gameEntity.getPlayers().add(user);
        return gameEntityToDTO(gameRepository.save(gameEntity));
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

    private GameDTO gameEntityToDTO(Game entity) {
        GameDTO dto = new GameDTO();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setStatus(entity.getStatus());
        dto.setAdmin(playerEntityToDTO(entity.getAdmin()));
        dto.setPlayers(entity.getPlayers()
                .stream()
                .map(this::playerEntityToDTO)
                .collect(Collectors.toSet()));

        return dto;
    }

    private PlayerDTO playerEntityToDTO(User player) {
        PlayerDTO dto = new PlayerDTO();

        dto.setId(player.getId());
        dto.setName(player.getName());

        return dto;
    }

}
