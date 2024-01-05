package org.springframework.samples.dwarf.invitation;

import java.time.LocalDateTime;

import org.springframework.samples.dwarf.game.Game;
import org.springframework.samples.dwarf.model.BaseEntity;
import org.springframework.samples.dwarf.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Invitation extends BaseEntity {
    
    private Boolean accepted;

    private LocalDateTime sendTime;

    @ManyToOne
    private User sender;
    
	@ManyToOne
    private User receiver;

    @ManyToOne
    private Game game;
}