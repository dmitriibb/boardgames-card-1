package com.dmbb.boardgame.cards.model.entity;

import com.dmbb.boardgame.cards.model.enums.GameStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private List<Card> cards = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Column(name = "main_player_id")
    private int mainPlayerId;

    @Column(name = "active_player_id")
    private int activePlayerId;

    private String password;

}
