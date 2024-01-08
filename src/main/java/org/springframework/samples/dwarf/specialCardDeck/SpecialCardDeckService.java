package org.springframework.samples.dwarf.specialCardDeck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.dwarf.card.SpecialCard;
import org.springframework.samples.dwarf.card.SpecialCardRepository;
import org.springframework.samples.dwarf.exceptions.ResourceNotFoundException;
import org.springframework.samples.dwarf.game.Game;
import org.springframework.samples.dwarf.mainboard.MainBoard;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class SpecialCardDeckService {

    SpecialCardDeckRepository scdr;
    SpecialCardRepository scr;

    @Autowired
    public SpecialCardDeckService(SpecialCardDeckRepository scdr, SpecialCardRepository scr) {
        this.scdr = scdr;
        this.scr = scr;
    }

    @Transactional(readOnly = true)
    public List<SpecialCardDeck> findAll() {
        return (List<SpecialCardDeck>) scdr.findAll();
    }

    @Transactional(readOnly = true)
    public SpecialCardDeck getSpecialCardDeckById(Integer id) {
        SpecialCardDeck res = scdr.findById(id).orElseThrow(() -> new ResourceNotFoundException("CardDeck", "Id", id));
        if (res.equals(null)) {
            throw new NullPointerException();
        } else {
            return res;
        }
    }

    @Transactional
    public SpecialCardDeck initialize() throws DataAccessException {
        // Not tested
        SpecialCardDeck scd = new SpecialCardDeck();

        ArrayList<SpecialCard> cards = new ArrayList<SpecialCard>();
        cards.addAll(scr.findAll());

        /*
        // TODO: test this
        for (int i = 1; i < 4; i++) {
            cards.remove(0);
        }*/

        Collections.shuffle(cards);

        scd.setSpecialCards(cards);

        saveSpecialCardDeck(scd);
        return scd;
    }

    @Transactional
    public SpecialCard getSpecialCard(MainBoard mb) {
        SpecialCardDeck speccarddeck = mb.getSpecialCardDeck(); 

        ArrayList<SpecialCard> cards = new ArrayList<>();
        cards.addAll(speccarddeck.getSpecialCards());

        SpecialCard res = cards.get(0);
        cards.remove(res);

        speccarddeck.setSpecialCards(cards);
        saveSpecialCardDeck(speccarddeck);

        return res;
    }

    @Transactional
    public SpecialCardDeck saveSpecialCardDeck(@Valid SpecialCardDeck scd) {
        if (scd.equals(null)) {
            throw new NullPointerException();
        } else {
            scdr.save(scd);
        }
        return scd;
    }

    @Transactional
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