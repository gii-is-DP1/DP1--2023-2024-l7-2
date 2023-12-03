package org.springframework.samples.dwarf.game;

import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.object.Object;
import org.springframework.samples.dwarf.card.SpecialCard;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecialCardRequestHandler {
    
    @NotNull
    private SpecialCard specialCard;
    @NotNull
    private Boolean usesBothDwarves; // Si no usa los dos dwarves se le quitan 4 medallas
    
    // Necesario para carta turnBack
    private Integer position;

    // Necesario para la carta Past glories
    private Card pastCard;

    //Necesario para special order
    Integer selectedGold;
    Integer selectedIron;
    Integer selectedSteal;
    Object selectedObject;

}
