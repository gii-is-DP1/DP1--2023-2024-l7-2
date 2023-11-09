package org.springframework.samples.petclinic.mainboard;

import java.util.Collection;

import org.springframework.samples.petclinic.cardDeck.CardDeck;
import org.springframework.samples.petclinic.model.NamedEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "MainBoard")
public class MainBoard extends NamedEntity{
    @NotBlank
    private Integer x;
    private Integer y;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<CardDeck> cardDecks;
}
