package org.springframework.samples.dwarf.statistic;

import org.springframework.samples.dwarf.model.NamedEntity;

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

    // The achievement is based on one or more of this variables
    private Integer totalIron;
    private Integer totalGold;
    private Integer totalSteal;
    private Integer totalObjects;
    private Integer totalMatches;
    private Integer totalVictories;
}
