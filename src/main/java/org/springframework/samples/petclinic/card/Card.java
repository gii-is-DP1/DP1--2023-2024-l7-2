package org.springframework.samples.petclinic.card;

import org.springframework.samples.petclinic.model.NamedEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "card")
public class Card extends NamedEntity {
    @NotBlank
    private String description;
    private Integer position;
    private String badgeImage;

    private Integer totalIron;
    private Integer totalGold;
    private Integer totalSteal;
    private Integer totalMedals;
    private Boolean helpCard;

}
