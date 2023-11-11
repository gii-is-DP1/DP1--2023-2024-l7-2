package org.springframework.samples.petclinic.specialCardDeck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.card.SpecialCard;
import org.springframework.samples.petclinic.card.SpecialCardRepository;
import org.springframework.samples.petclinic.card.SpecialCardService;
import org.springframework.samples.petclinic.exceptions.ResourceNotFoundException;
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

    @Transactional()
    public List<SpecialCardDeck> initialize() {

        // We shuffle all special cards
        ArrayList<SpecialCard> specCards = new ArrayList<SpecialCard>();
        specCards.addAll(scs.getSpecialCards());
        Collections.shuffle(specCards);

        // We split them into three decks and we create three special card deck
        List<SpecialCard> specCardList1 = List.of(specCards.get(0),specCards.get(1),specCards.get(2));
        List<SpecialCard> specCardList2 = List.of(specCards.get(3),specCards.get(4),specCards.get(5));
        List<SpecialCard> specCardList3 = List.of(specCards.get(6),specCards.get(7),specCards.get(8));

        SpecialCardDeck scd1 = new SpecialCardDeck();
        SpecialCardDeck scd2 = new SpecialCardDeck();
        SpecialCardDeck scd3 = new SpecialCardDeck();

        scd1.setSpecialCards(specCardList1);
        scd1.setLastSpecialCard(specCardList1.get(0));

        scd2.setSpecialCards(specCardList2);
        scd2.setLastSpecialCard(specCardList2.get(0));

        scd3.setSpecialCards(specCardList3);
        scd3.setLastSpecialCard(specCardList3.get(0));

        return List.of(scd1, scd2, scd3);
    }


    @Transactional()
    public SpecialCard getSpecialCard(Integer id) {
        List<SpecialCard> cards = getSpecialCardDeckById(id).getSpecialCards();

        //Collections.shuffle(cards);

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
    public SpecialCardDeck updateSpecialCardDeck(@Valid SpecialCardDeck specialcardDeck, int id) {
        SpecialCardDeck scd = getSpecialCardDeckById(id);
        BeanUtils.copyProperties(scd, specialcardDeck);
        return saveSpecialCardDeck(scd);
    }

}