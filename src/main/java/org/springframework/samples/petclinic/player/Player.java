package org.springframework.samples.petclinic.player;

import org.springframework.samples.petclinic.user.User;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

import org.springframework.samples.petclinic.dwarf.Dwarf;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.model.NamedEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Player")
public class Player extends NamedEntity {
    @NotBlank
    private String color;
    private Integer steal;
    private Integer gold;
    private Integer iron;
    private Integer medal;

    @ManyToOne(optional = true)
    private User user;

    @JsonSerialize(using = gameSerializer.class)
    @JsonDeserialize(using = gameDeserializer.class)
    @ManyToOne(optional = true)
    private Game game;

    /* 
    @OneToMany(mappedBy="player")
    List<Dwarf> dwarfs;*/
}
