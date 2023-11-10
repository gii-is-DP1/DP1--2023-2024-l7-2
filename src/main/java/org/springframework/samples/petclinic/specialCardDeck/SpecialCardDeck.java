package org.springframework.samples.petclinic.specialCardDeck;

import java.util.List;

import org.springframework.samples.petclinic.card.SpecialCard;
import org.springframework.samples.petclinic.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "specialCardDeck")
public class SpecialCardDeck extends BaseEntity {

    @OneToMany()
    @JoinColumn(name = "special_card")
    List<SpecialCard> specialCards;

    @OneToOne
    //@Transient
    @JoinColumn(name = "card_id")
    SpecialCard lastSpecialCard;

}