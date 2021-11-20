package com.dmbb.boardgame.cards.repository;

import com.dmbb.boardgame.cards.model.entity.CardDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardDescriptionRepository extends JpaRepository<CardDescription, Integer> {
}
