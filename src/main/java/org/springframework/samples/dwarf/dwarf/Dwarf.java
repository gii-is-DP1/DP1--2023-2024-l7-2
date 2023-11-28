package org.springframework.samples.dwarf.dwarf;

import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.model.BaseEntity;
import org.springframework.samples.dwarf.player.Player;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Dwarf")
public class Dwarf extends BaseEntity {

    Integer round;

    @ManyToOne(optional = false)
    Player player;

    @ManyToOne(optional = true)
    Card card;
}
