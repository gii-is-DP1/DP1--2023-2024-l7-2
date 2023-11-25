package org.springframework.samples.dwarf.specialCardDeck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional()
    public ArrayList<SpecialCardDeck> initialize() {

        // We shuffle all special cards
        ArrayList<SpecialCard> specCards = new ArrayList<SpecialCard>();
        specCards.addAll(scs.getSpecialCards());
        Collections.shuffle(specCards);

        // We split them into three decks and we create three special card deck
        // List<SpecialCard> specCardList1 =
        // List.of(specCards.get(0),specCards.get(1),specCards.get(2));
        // Por alguna razon si se hace con List no funciona el saveSpecialCardDeck :)
        ArrayList<SpecialCard> specCardList1 = new ArrayList<SpecialCard>();
        specCardList1.add(specCards.get(0));
        specCardList1.add(specCards.get(1));
        specCardList1.add(specCards.get(2));

        ArrayList<SpecialCard> specCardList2 = new ArrayList<SpecialCard>();
        specCardList2.add(specCards.get(3));
        specCardList2.add(specCards.get(4));
        specCardList2.add(specCards.get(5));

        ArrayList<SpecialCard> specCardList3 = new ArrayList<SpecialCard>();
        specCardList3.add(specCards.get(6));
        specCardList3.add(specCards.get(7));
        specCardList3.add(specCards.get(8));

        SpecialCardDeck scd1 = initializeOneCardDeck(specCardList1);// new SpecialCardDeck();
        SpecialCardDeck scd2 = initializeOneCardDeck(specCardList2);
        SpecialCardDeck scd3 = initializeOneCardDeck(specCardList3);
        /*
         * scd1.setLastSpecialCard(specCardList1.get(0));
         * scd1.setSpecialCards(specCardList1);
         * saveSpecialCardDeck(scd1);
         * 
         * scd2.setLastSpecialCard(specCardList2.get(0));
         * scd2.setSpecialCards(specCardList2);
         * saveSpecialCardDeck(scd2);
         * 
         * scd3.setLastSpecialCard(specCardList3.get(0));
         * scd3.setSpecialCards(specCardList3);
         * saveSpecialCardDeck(scd3);
         */
        ArrayList<SpecialCardDeck> res = new ArrayList<SpecialCardDeck>();
        res.add(scd1);
        res.add(scd2);
        res.add(scd3);

        return res;
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