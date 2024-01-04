package org.springframework.samples.dwarf.game;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.samples.dwarf.dwarf.Dwarf;
import org.springframework.samples.dwarf.mainboard.MainBoard;
import org.springframework.samples.dwarf.model.NamedEntity;
import org.springframework.samples.dwarf.player.Player;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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

    private LocalDateTime start;
    private LocalDateTime finish;

    Integer winner_id;
    Integer round;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private MainBoard mainBoard;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.DETACH })
    private List<Dwarf> dwarves;

    @OneToMany
    private List<Player> players;


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Player playerCreator;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Player playerStart;

    @NotNull
    Boolean isPublic = false;

}
