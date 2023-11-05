package org.springframework.samples.petclinic.game;

import java.time.LocalDateTime;

import org.springframework.samples.petclinic.model.NamedEntity;
import org.springframework.samples.petclinic.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
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
    @ManyToOne(optional=true)
    private User winner_id;
    */
    
    Integer winner_id;
    Integer round;
}
