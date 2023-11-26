package org.springframework.samples.dwarf.mainboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.CardService;
import org.springframework.samples.dwarf.card.SpecialCard;
import org.springframework.samples.dwarf.card.SpecialCardService;
import org.springframework.samples.dwarf.cardDeck.CardDeck;
import org.springframework.samples.dwarf.cardDeck.CardDeckService;
import org.springframework.samples.dwarf.specialCardDeck.SpecialCardDeck;
import org.springframework.samples.dwarf.specialCardDeck.SpecialCardDeckService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class MainBoardService {

    MainBoardRepository repo;
    private final CardDeckService cds;
    private final SpecialCardDeckService scds;
    private final SpecialCardService scs;
    private final CardService cs;

    @Autowired
    public MainBoardService(MainBoardRepository repo, CardDeckService cds, SpecialCardDeckService scds,
            SpecialCardService scs, CardService cs) {
        this.repo = repo;
        this.cds = cds;
        this.scds = scds;
        this.scs = scs;
        this.cs = cs;
    }

    @Transactional(readOnly = true)
    public List<MainBoard> getMainBoard() {
        return repo.findAll();
    }

    @Transactional
    public MainBoard saveMainBoard(@Valid MainBoard newMainBoard) {
        return repo.save(newMainBoard);
    }

    @Transactional(readOnly = true)
    public MainBoard getById(int id) {
        Optional<MainBoard> result = repo.findById(id);
        return result.isPresent() ? result.get() : null;
    }

    @Transactional()
    public MainBoard initialize() {

        CardDeck cardDecks = cds.initialiate();
        SpecialCardDeck specCardDeck = scds.initialize();

        MainBoard mb = new MainBoard();
        mb.setCardDeck(cardDecks);
        mb.setSpecialCardDeck(specCardDeck);

        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = 1; i <= 9; i++) {
            Card a = cs.getById(i);
            System.out.println(a);
            cards.add(a);
        }
        mb.setCards(cards);
        System.out.println(cards);

        ArrayList<SpecialCard> sCards = new ArrayList<SpecialCard>();
        for (int i = 1; i <= 3; i++) {
            SpecialCard a = scs.getById(i);
            System.out.println(a);
            sCards.add(a);
        }
        mb.setSCards(sCards);
        System.out.println(sCards);
        saveMainBoard(mb);

        return mb;
    }

    // @Transactional
    // public MainBoard numberOfSpecialCards(@Valid MainBoard mb, @Valid
    // SpecialCardDeck sc) {
    // if (mb.getSpecialCardDeck().size() == 3) {
    // return mb;
    // } else {
    // SpecialCard lastSpecialCard = sc.getLastSpecialCard();
    // for (SpecialCardDeck specialCardDeck : mb.getSpecialCardDeck()) {
    // specialCardDeck.getSpecialCards().add(lastSpecialCard);

    // }
    // return mb;
    // }
    // }
}
