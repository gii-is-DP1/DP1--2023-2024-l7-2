package org.springframework.samples.petclinic.cardDeck;

import java.util.List;

import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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

    @OneToOne
    //@Transient
    @JoinColumn(name = "card_id")
    Card lastCard;

}
