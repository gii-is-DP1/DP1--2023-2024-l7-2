package org.springframework.samples.dwarf.location;

import java.util.List;

import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Location extends BaseEntity{
    
    @NotNull
    @Min(1)
    @Max(9)
    Integer position;

    @ManyToMany
    private List<Card> cards;
}
