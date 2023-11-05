package org.springframework.samples.petclinic.cardDeck;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.exceptions.ResourceNotFoundException;
import org.springframework.samples.petclinic.pet.Pet;
import org.springframework.samples.petclinic.user.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/cardDeck")
@Tag(name = "CardDeck", description = "The management API for the Card Decks")
@SecurityRequirement(name = "bearerAuth")
public class CardDeckController {

    private final CardDeckService cds;

    @Autowired
    public CardDeckController(CardDeckService cds) {
        this.cds = cds;
    }

    @GetMapping
    public ResponseEntity<List<CardDeck>> findAll() {
        return new ResponseEntity<>(cds.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardDeck> getCardDeckById(@PathVariable("id") Integer id) {
        CardDeck cd = cds.getCardDeckById(id);
        return new ResponseEntity<>(cd, HttpStatus.OK);
    }

    @GetMapping("/getTwoCards/{id}")
    public ResponseEntity<List<Card>> getTwoCards(@PathVariable("id") Integer id) {
        List<Card> cd = cds.getTwoCards(id);
        return new ResponseEntity<>(cd, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardDeck> updateCardDeck(@PathVariable("id") int id, @Valid @RequestBody CardDeck cardDeck) {
        CardDeck updatedCardDeck = cds.updateCardDeck(cardDeck, id);
        return new ResponseEntity<>(updatedCardDeck, HttpStatus.OK);
    }

}