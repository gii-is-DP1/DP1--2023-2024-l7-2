package org.springframework.samples.dwarf.specialCardDeck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.SpecialCard;
import org.springframework.samples.dwarf.card.SpecialCardRepository;
import org.springframework.samples.dwarf.card.SpecialCardService;
import org.springframework.samples.dwarf.cardDeck.CardDeck;
import org.springframework.samples.dwarf.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class SpecialCardDeckService {

    SpecialCardDeckRepository scdr;
    SpecialCardRepository scr;
    SpecialCardService scs;

    @Autowired
    public SpecialCardDeckService(SpecialCardDeckRepository scdr, SpecialCardService scs) {
        this.scdr = scdr;
        this.scs = scs;
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
    public SpecialCardDeck initializeOneCardDeck(ArrayList<SpecialCard> cards) {
        SpecialCardDeck scd = new SpecialCardDeck();
        scd.setLastSpecialCard(cards.get(0));
        scd.setSpecialCards(cards);
        saveSpecialCardDeck(scd);
        return scd;
    }

    @Transactional
    public SpecialCardDeck initialize() throws DataAccessException {
        // Not tested
        SpecialCardDeck scd = new SpecialCardDeck();

        ArrayList<SpecialCard> specialCards = new ArrayList<SpecialCard>();
        specialCards.addAll(scr.findAll());

        // TODO: test this
        for (int i = 1; i < 10; i++) {
            specialCards.remove(0);
        }

        Collections.shuffle(specialCards);

        scd.setSpecialCards(specialCards);
        scd.setLastSpecialCard(specialCards.get(0));

        saveSpecialCardDeck(scd);
        return scd;
    }

    @Transactional()
    public SpecialCard getSpecialCard(Integer id) {
        List<SpecialCard> cards = getSpecialCardDeckById(id).getSpecialCards();

        // Collections.shuffle(cards);

        SpecialCard lastCard = getSpecialCardDeckById(id).getLastSpecialCard();
        Integer lastCardIndex = cards.indexOf(lastCard);

        SpecialCardDeck newScd = getSpecialCardDeckById(id);
        newScd.setLastSpecialCard(cards.get(lastCardIndex + 1));
        updateSpecialCardDeck(newScd, id);

        return cards.get(lastCardIndex);
    }

    @Transactional()
    public SpecialCardDeck saveSpecialCardDeck(@Valid SpecialCardDeck newSpecialCardDeck) {
        scdr.save(newSpecialCardDeck);
        return newSpecialCardDeck;
    }

    @Transactional()
    public SpecialCardDeck updateSpecialCardDeck(@Valid SpecialCardDeck scd, int sCardDeckId) {
        SpecialCardDeck sCarddeck = getSpecialCardDeckById(sCardDeckId);

        if (sCarddeck != null) {
            saveSpecialCardDeck(scd);
            return scd;
        } else {
            return null;
        }

    }

}