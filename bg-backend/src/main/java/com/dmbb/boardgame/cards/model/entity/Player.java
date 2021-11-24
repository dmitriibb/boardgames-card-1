package com.dmbb.boardgame.cards.model.entity;

import com.dmbb.boardgame.cards.model.enums.PlayerStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "players")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    private PlayerStatus status;

    @Column(name = "player_order")
    private int order;

    private int coins;

    private int points;

    private int swords;

    private int crosses;

    private int houses;

    private int anchors;

    @OneToMany(mappedBy = "player")
    private Set<Card> cards = new HashSet<>();

}
