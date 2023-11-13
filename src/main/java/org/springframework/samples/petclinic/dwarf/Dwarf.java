package org.springframework.samples.petclinic.dwarf;

import java.util.List;

import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.model.NamedEntity;
import org.springframework.samples.petclinic.player.Player;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Dwarf")
public class Dwarf extends NamedEntity {

    Integer round;

    //@JsonSerialize(using = PlayerSerializer.class)
    //@JsonDeserialize(using = PlayerDeserializer.class)
    @OneToOne(optional = true)
    Player player;

    @OneToMany
    List<Card> cards;
}
