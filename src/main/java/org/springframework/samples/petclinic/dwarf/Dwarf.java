package org.springframework.samples.petclinic.dwarf;
import java.util.List;

import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.model.NamedEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Dwarf")
public class Dwarf extends NamedEntity {

    @OneToMany
    private List<Card> cards;
}
