package org.springframework.samples.dwarf.card;

import org.springframework.samples.dwarf.model.NamedEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "card_type")
public class CardType extends NamedEntity {

}
