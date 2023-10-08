package org.springframework.samples.petclinic.statistic;

import org.springframework.samples.petclinic.model.NamedEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "achievement")
public class Achievement extends NamedEntity {
    @NotBlank
    private String description;
    private String badgeImage;
}
