package com.dmbb.boardgame.cards.model.entity;

import com.dmbb.boardgame.cards.model.dto.PlayerShortDTO;
import com.dmbb.boardgame.cards.model.enums.CardStatus;
import com.dmbb.boardgame.cards.model.enums.PlayerStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "players")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

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

    public PlayerShortDTO toDTO() {
        PlayerShortDTO dto = new PlayerShortDTO();
        dto.setId(getId());
        dto.setName(getUser().getName());
        dto.setCoins(getCoins());
        dto.setPoints(getPoints());
        dto.setAnchors(getAnchors());
        dto.setCrosses(getCrosses());
        dto.setHouses(getHouses());
        dto.setSwords(getSwords());
        dto.setCards(getCards()
                .stream()
                .filter(card -> card.getStatus() == CardStatus.PLAYER_TABLE)
                .map(Card::toDTO)
                .collect(Collectors.toList()));
        dto.setUsername(user.getUsername());
        return dto;
    }

}
