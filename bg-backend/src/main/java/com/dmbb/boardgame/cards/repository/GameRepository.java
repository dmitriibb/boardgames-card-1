package com.dmbb.boardgame.cards.repository;

import com.dmbb.boardgame.cards.model.entity.Game;
import com.dmbb.boardgame.cards.model.entity.User;
import com.dmbb.boardgame.cards.model.enums.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {

    List<Game> findAllByAdmin(User admin);

    @Query("select g from Game g where g.status = :status and g in (select p.game from Player p where p.user = :user)")
    List<Game> getGamesWithStatusForUser(GameStatus status, User user);

    @Query("select " +
            "   case " +
            "       when count(1) > 0 " +
            "       then true " +
            "       else false " +
            "   end " +
            "from Game g " +
            "join Player p on g.activePlayerId = p.id " +
            "join User u on p.user = u " +
            "where u = :user " +
            "and g = :game")
    boolean userIsActivePlayer(User user, Game game);

    @Query("select " +
            "   case " +
            "       when count(1) > 0 " +
            "       then true " +
            "       else false " +
            "   end " +
            "from Game g " +
            "join Player p on g.mainPlayerId = p.id " +
            "join User u on p.user = u " +
            "where u = :user " +
            "and g = :game")
    boolean userIsMainPlayer(User user, Game game);

    @Query("select " +
            "   case " +
            "       when count(1) > 0 " +
            "       then true " +
            "       else false " +
            "   end " +
            "from Game g " +
            "join Player p on g.activePlayerId = p.id " +
            "   and g.mainPlayerId = p.id " +
            "join User u on p.user = u " +
            "where u = :user " +
            "and g = :game")
    boolean userIsActiveAndMainPlayer(User user, Game game);

}
