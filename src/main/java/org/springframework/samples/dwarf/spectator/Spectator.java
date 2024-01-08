package org.springframework.samples.dwarf.spectator;

import org.springframework.samples.dwarf.model.NamedEntity;
import org.springframework.samples.dwarf.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Spectator extends NamedEntity {
    @NotBlank
    private String color;

    @ManyToOne(optional = true)
    private User user;

}
