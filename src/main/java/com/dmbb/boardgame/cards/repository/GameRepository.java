package com.dmbb.boardgame.cards.repository;

import com.dmbb.boardgame.cards.model.entity.Game;
import com.dmbb.boardgame.cards.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {

    List<Game> findAllByAdmin(User admin);

}
