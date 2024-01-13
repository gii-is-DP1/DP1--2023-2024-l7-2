package org.springframework.samples.dwarf.player;


import java.util.List;

import org.springframework.samples.dwarf.model.NamedEntity;
import org.springframework.samples.dwarf.object.Object;
import org.springframework.samples.dwarf.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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

    @ManyToMany
    List<Object> objects;

}
