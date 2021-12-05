package com.dmbb.boardgame.cards.model.entity;

import com.dmbb.boardgame.cards.model.dto.CardDTO;
import com.dmbb.boardgame.cards.model.enums.CardStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "cards")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @JoinColumn(name = "card_description_id")
    private int cardDescriptionId;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    private boolean coin;

    @Enumerated(EnumType.STRING)
    private CardStatus status;

    @Column(name = "card_order")
    private int cardOrder;

    public CardDTO toDTO() {
        CardDTO dto = new CardDTO();
        dto.setId(id);
        dto.setDescriptionId(cardDescriptionId);
        dto.setStatus(status);
        return dto;
    }

}
