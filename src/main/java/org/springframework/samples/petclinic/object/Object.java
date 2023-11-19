package org.springframework.samples.petclinic.object;

import org.springframework.samples.petclinic.model.NamedEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Object")
public class Object extends NamedEntity {
    @NotBlank
    @NotNull
    private String name;
    private String photo;
}
