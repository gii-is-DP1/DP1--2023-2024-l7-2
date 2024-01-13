package org.springframework.samples.dwarf.game;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.samples.dwarf.dwarf.Dwarf;
import org.springframework.samples.dwarf.mainboard.MainBoard;
import org.springframework.samples.dwarf.model.NamedEntity;
import org.springframework.samples.dwarf.player.Player;
import org.springframework.samples.dwarf.spectator.Spectator;
import org.springframework.samples.dwarf.statistic.Achievement;
import org.springframework.samples.dwarf.chat.Chat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dwarf> dwarves;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Spectator> spectators;

    @OneToOne
    private Player playerCreator;

    @OneToOne
    private Player playerStart;

    @NotNull
    Boolean isPublic = false;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    Chat chat;

    @OneToMany
 	private Collection<Achievement> achievements;


}
