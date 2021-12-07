package com.dmbb.boardgame.cards.repository;


import com.dmbb.boardgame.cards.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    User getByEmail(String email);

}
