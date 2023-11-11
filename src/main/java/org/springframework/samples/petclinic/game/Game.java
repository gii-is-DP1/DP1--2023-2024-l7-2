package org.springframework.samples.petclinic.game;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.samples.petclinic.dwarf.Dwarf;
import org.springframework.samples.petclinic.mainboard.MainBoard;
import org.springframework.samples.petclinic.model.NamedEntity;
import org.springframework.samples.petclinic.player.Player;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "game")
public class Game extends NamedEntity {
    String name;

    @Column(unique = true)
    String code;

    LocalDateTime start;
    LocalDateTime finish;

    /*
     * @ManyToOne(optional=true)
     * private User winner_id;
     */

    Integer winner_id;
    Integer round;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private MainBoard mainBoard;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dwarf> dwarves;
}
