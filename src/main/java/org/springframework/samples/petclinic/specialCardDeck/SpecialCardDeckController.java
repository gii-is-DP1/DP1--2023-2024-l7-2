package org.springframework.samples.petclinic.specialCardDeck;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.card.SpecialCard;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/specialCardDeck")
@Tag(name = "SpecialCardDeck", description = "The management API for the Special Card Decks")
@SecurityRequirement(name = "bearerAuth")

public class SpecialCardDeckController {

    private final SpecialCardDeckService scds;

    @Autowired
    public SpecialCardDeckController(SpecialCardDeckService scds) {
        this.scds = scds;
    }

    @GetMapping
    public ResponseEntity<List<SpecialCardDeck>> findAll() {
        return new ResponseEntity<>(scds.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecialCardDeck> getSpecialCardDeckById(@PathVariable("id") Integer id) {
        SpecialCardDeck scd = scds.getSpecialCardDeckById(id);
        return new ResponseEntity<>(scd, HttpStatus.OK);
    }
/*
    @GetMapping("/{id}")
    public ResponseEntity<SpecialCard> getSpecialCard(@PathVariable("id") Integer id) {
        SpecialCard sc = scds.getSpecialCard(id);
        return new ResponseEntity<>(sc, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpecialCardDeck> updateCardDeck(@PathVariable("id") int id,
            @Valid @RequestBody SpecialCardDeck cardDeck) {
        SpecialCardDeck updatedCardDeck = scds.updateSpecialCardDeck(cardDeck, id);
        return new ResponseEntity<>(updatedCardDeck, HttpStatus.OK);
    }
 */
}