package org.springframework.samples.dwarf.mainboard;

import java.util.ArrayList;
import java.util.List;

import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.SpecialCard;
import org.springframework.samples.dwarf.cardDeck.CardDeck;
import org.springframework.samples.dwarf.location.Location;
import org.springframework.samples.dwarf.model.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MainBoard extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private CardDeck cardDeck;

    // @OneToOne
    // private SpecialCardDeck specialCardDeck;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Location> locations;

    @ManyToMany
    private List<SpecialCard> sCards;

    public List<Card> getCards() {
        ArrayList<Card> res = new ArrayList<>();
        for (Location lt:this.locations) {
            List<Card> locationCards = lt.getCards();
            Integer locationCardsLength = locationCards.size();
            res.add(locationCards.get(locationCardsLength-1));
        }

        return res;
    }

    public List<Card> getLocationCards(List<Location> locationsPassed) {
        ArrayList<Card> res = new ArrayList<>();
        for (Location lt:locationsPassed) {
            List<Card> locationCards = lt.getCards();
            Integer locationCardsLength = locationCards.size();
            res.add(locationCards.get(locationCardsLength-1));
        }

        return res;
    }
}
