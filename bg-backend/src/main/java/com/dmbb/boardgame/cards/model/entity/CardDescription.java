package com.dmbb.boardgame.cards.model.entity;

import com.dmbb.boardgame.cards.model.dto.CardDescriptionDTO;
import com.dmbb.boardgame.cards.model.enums.CardColor;
import com.dmbb.boardgame.cards.model.enums.CardType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "card_descriptions")
public class CardDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private CardType type;

    private int points;

    private int coins;

    private CardColor color;

    private int houses;

    private int crosses;

    private int anchors;

    private int swords;

    private int amount;

    @Column(name = "image_name")
    private String imageName;

    public CardDescriptionDTO toDTO() {
        CardDescriptionDTO dto = new CardDescriptionDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setType(type);
        dto.setPoints(points);
        dto.setCoins(coins);
        dto.setColor(color);
        dto.setHouses(houses);
        dto.setCrosses(crosses);
        dto.setAnchors(anchors);
        dto.setSwords(swords);
        dto.setImageName(imageName);
        return dto;
    }

}
