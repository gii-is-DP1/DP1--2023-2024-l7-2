package org.springframework.samples.dwarf.cardDeck;

import java.util.List;

import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cardDeck")
public class CardDeck extends BaseEntity {

    @ManyToMany()
    @JoinColumn(name = "card")
    List<Card> cards;

    @ManyToOne
    // @Transient
    @JoinColumn(name = "card_id")
    Card lastCard;

}
