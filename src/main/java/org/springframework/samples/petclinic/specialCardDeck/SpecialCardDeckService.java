package org.springframework.samples.petclinic.specialCardDeck;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.card.SpecialCard;
import org.springframework.samples.petclinic.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class SpecialCardDeckService {

    SpecialCardDeckRepository scdr;

    @Autowired
    public SpecialCardDeckService(SpecialCardDeckRepository scdr) {
        this.scdr = scdr;
    }

    @Transactional(readOnly = true)
    public List<SpecialCardDeck> findAll() {
        return (List<SpecialCardDeck>) scdr.findAll();
    }

    @Transactional(readOnly = true)
    public SpecialCardDeck getSpecialCardDeckById(Integer id) {
        return scdr.findById(id).orElseThrow(() -> new ResourceNotFoundException("CardDeck", "Id", id));
    }

    @Transactional
    public SpecialCardDeck updateSpecialCardDeck(@Valid SpecialCardDeck scd, int specialCardDeckId) {
        return scdr.updateSpecialCardDeck(scd, specialCardDeckId);
    }

    @Transactional(readOnly = true)
    public SpecialCard getSpecialCard(Integer id) {

        List<SpecialCard> cards = getSpecialCardDeckById(id).getSpecialCards();

        Collections.shuffle(cards);

        SpecialCard lastCard = getSpecialCardDeckById(id).getLastSpecialCard();
        Integer lastCardIndex = cards.indexOf(lastCard);

        SpecialCardDeck newScd = getSpecialCardDeckById(id);
        newScd.setLastSpecialCard(cards.get(lastCardIndex + 1));
        updateSpecialCardDeck(newScd, id);

        return cards.get(lastCardIndex);
    }

}