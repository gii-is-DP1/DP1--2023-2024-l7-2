package org.springframework.samples.dwarf.mainboard;

import java.util.List;

import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.SpecialCard;
import org.springframework.samples.dwarf.cardDeck.CardDeck;
import org.springframework.samples.dwarf.model.BaseEntity;
import org.springframework.samples.dwarf.specialCardDeck.SpecialCardDeck;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "MainBoard")
public class MainBoard extends BaseEntity {

    @OneToOne
    private CardDeck cardDeck;

    @OneToMany
    private List<SpecialCardDeck> specialCardDecks;

    @ManyToMany
    private List<Card> cards;

    @ManyToMany
    private List<SpecialCard> sCards;
}
