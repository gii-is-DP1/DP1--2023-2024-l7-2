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
    @NotBlank
    private Integer position;
    @NotBlank
    private Boolean specialCard;
   


    // The achievement is based on one or more of this variables
    private Integer totalIron;
    private Integer totalGold;
    private Integer totalSteal;
    private Boolean helpCard;
    
    
}
