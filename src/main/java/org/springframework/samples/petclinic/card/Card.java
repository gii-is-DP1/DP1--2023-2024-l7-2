package org.springframework.samples.petclinic.card;

import org.springframework.samples.petclinic.model.NamedEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @NotBlank
    private Integer position;
    // private String badgeImage;

    @ManyToOne(optional = false)
    @JoinColumn(name = "card_type_id")
    private CardType cardType;

    private Integer totalIron;
    private Integer totalGold;
    private Integer totalSteal;
    private Integer totalMedals;

}
