package org.springframework.samples.dwarf.dwarf;

import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.model.BaseEntity;
import org.springframework.samples.dwarf.player.Player;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Dwarf extends BaseEntity {

    Integer round;

    @ManyToOne(optional = true)
    Player player;

    @ManyToOne(optional = true)
    Card card;

    Boolean needsToBeResolved = true;
}
