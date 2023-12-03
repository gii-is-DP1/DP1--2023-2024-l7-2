package org.springframework.samples.dwarf.card;

import org.springframework.samples.dwarf.model.NamedEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SpecialCard extends NamedEntity {
    @NotBlank
    private String description;
    // private String badgeImage;

    @OneToOne
    private Card turnedSide;
}