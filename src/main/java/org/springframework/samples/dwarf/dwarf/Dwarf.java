package org.springframework.samples.dwarf.dwarf;

import java.util.List;

import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.model.BaseEntity;
import org.springframework.samples.dwarf.player.Player;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Dwarf")
public class Dwarf extends BaseEntity {

    Integer round;

    // @JsonSerialize(using = PlayerSerializer.class)
    // @JsonDeserialize(using = PlayerDeserializer.class)
    @ManyToOne(optional = true)
    Player player;

    @ManyToMany
    List<Card> cards;
}
