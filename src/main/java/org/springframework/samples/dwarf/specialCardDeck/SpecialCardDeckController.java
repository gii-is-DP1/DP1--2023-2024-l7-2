package org.springframework.samples.dwarf.specialCardDeck;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.dwarf.card.SpecialCard;
import org.springframework.samples.dwarf.exceptions.BadRequestException;
import org.springframework.samples.dwarf.exceptions.ResourceNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        if (scd == null)
            throw new ResourceNotFoundException("No encuentro el cardDeck que me has pedido con id:" + id);
        return new ResponseEntity<>(scd, HttpStatus.OK);

    }

    /*
    @GetMapping("/getSpecialCard/{id}")
    public ResponseEntity<SpecialCard> getSpecialCard(@PathVariable("id") Integer id) {
        SpecialCard scd = scds.getSpecialCard(id);
        if (scd == null)
            throw new ResourceNotFoundException("No encuentro el cardDeck que me has pedido con id:" + id);
        return new ResponseEntity<>(scd, HttpStatus.OK);
    }*/

    @PostMapping
    public ResponseEntity<SpecialCardDeck> createSpecialCardDeck(@RequestBody @Valid SpecialCardDeck newSpecialCardDeck,
            BindingResult br) {
        SpecialCardDeck result = null;
        if (!br.hasErrors())
            result = scds.saveSpecialCardDeck(newSpecialCardDeck);
        else
            throw new BadRequestException(br.getAllErrors());
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpecialCardDeck> updateSpecialCardDeck(@PathVariable("id") int id,
            @Valid @RequestBody SpecialCardDeck specialcardDeck, BindingResult br) {
        if (br.hasErrors())
            throw new BadRequestException(br.getAllErrors());
        SpecialCardDeck updatedSpecialCardDeck = scds.updateSpecialCardDeck(specialcardDeck, id);
        return new ResponseEntity<>(updatedSpecialCardDeck, HttpStatus.OK);
    }

}