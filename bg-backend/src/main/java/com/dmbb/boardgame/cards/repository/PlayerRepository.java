package com.dmbb.boardgame.cards.repository;

import com.dmbb.boardgame.cards.model.entity.Game;
import com.dmbb.boardgame.cards.model.entity.Player;
import com.dmbb.boardgame.cards.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Integer> {

    Optional<Player> findByUserAndGame(User user, Game game);

    @Query("select p.user.email from Player p where p.id = :playerId")
    String getUsernameByPlayerId(int playerId);

}
