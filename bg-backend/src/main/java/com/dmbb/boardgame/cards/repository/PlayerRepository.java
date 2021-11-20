package com.dmbb.boardgame.cards.repository;

import com.dmbb.boardgame.cards.model.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
}
