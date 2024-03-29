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
import org.springframework.samples.dwarf.user.User;
import org.springframework.samples.dwarf.chat.Chat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne(optional = true)
    private User userWinner;

    Integer round;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private MainBoard mainBoard;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.DETACH })
    private List<Dwarf> dwarves;

    @OneToMany
    private List<Player> players;

    @OneToMany
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
