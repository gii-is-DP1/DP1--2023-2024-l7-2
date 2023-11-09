package org.springframework.samples.petclinic.card;

import org.springframework.samples.petclinic.model.NamedEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "card_type")
public class CardType extends NamedEntity {

}
