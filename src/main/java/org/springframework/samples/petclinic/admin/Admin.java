package org.springframework.samples.petclinic.admin;

import org.springframework.samples.petclinic.model.NamedEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Admin")

public class Admin extends NamedEntity {
    @NotBlank
    private String name;
    private String password;
    private String profilePicture;
}
