package com.dmbb.boardgame.cards.model.entity;

import com.dmbb.boardgame.cards.model.enums.GameStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;

    @OneToMany(mappedBy = "game")
    private Set<Player> players = new HashSet<>();

    @OneToMany(mappedBy = "game")
    private Set<Card> cards = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private GameStatus status;

}
