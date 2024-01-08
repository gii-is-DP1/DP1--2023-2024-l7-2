package org.springframework.samples.dwarf.specialCardDeck;

import java.util.ArrayList;
import java.util.List;

import org.springframework.samples.dwarf.card.SpecialCard;
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
public class SpecialCardDeck extends BaseEntity {

    @ManyToMany()
    @JoinColumn(name = "special_card")
    List<SpecialCard> specialCards;
}