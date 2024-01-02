package org.springframework.samples.dwarf.player;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

import org.springframework.samples.dwarf.game.Game;
import org.springframework.samples.dwarf.model.NamedEntity;
import org.springframework.samples.dwarf.object.Object;
import org.springframework.samples.dwarf.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Player extends NamedEntity {
    @NotBlank
    private String color;
    private Integer steal;
    private Integer gold;
    private Integer iron;
    private Integer medal;

    private Boolean isEspectator;

    @ManyToOne(optional = true)
    private User user;

    @JsonSerialize(using = gameSerializer.class)
    @JsonDeserialize(using = gameDeserializer.class)
    @ManyToOne(optional = true)
    private Game game;

    /*
     * @OneToMany(mappedBy="player")
     * List<Dwarf> dwarfs;
     */

    @OneToMany
    List<Object> objects;

}
