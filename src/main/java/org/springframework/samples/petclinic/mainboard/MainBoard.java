package org.springframework.samples.petclinic.mainboard;

import org.springframework.samples.petclinic.model.NamedEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "MainBoard")
public class MainBoard extends NamedEntity{
    @NotBlank
    private Integer x;
    private Integer y;
}
