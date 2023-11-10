package org.springframework.samples.petclinic.mainboard;

import java.util.List;

import org.springframework.samples.petclinic.cardDeck.CardDeck;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.specialCardDeck.SpecialCardDeck;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "MainBoard")
public class MainBoard extends BaseEntity{

    @OneToOne
    private CardDeck cardDeck;

    @OneToMany
    private List<SpecialCardDeck> specialCardDecks;
}
