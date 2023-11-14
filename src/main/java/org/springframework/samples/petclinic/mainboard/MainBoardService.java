package org.springframework.samples.petclinic.mainboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.CardService;
import org.springframework.samples.petclinic.card.SpecialCard;
import org.springframework.samples.petclinic.cardDeck.CardDeck;
import org.springframework.samples.petclinic.cardDeck.CardDeckService;
import org.springframework.samples.petclinic.specialCardDeck.SpecialCardDeck;
import org.springframework.samples.petclinic.specialCardDeck.SpecialCardDeckService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class MainBoardService {
    
    MainBoardRepository repo;
    private final CardDeckService cds;
    private final SpecialCardDeckService scds;
    private final CardService cs;
    
    @Autowired
    public MainBoardService(MainBoardRepository repo, CardDeckService cds, SpecialCardDeckService scds, CardService cs) {
        this.repo = repo;
        this.cds = cds;
        this.scds = scds;
        this.cs = cs;
    }

    @Transactional(readOnly = true)
    public
    List<MainBoard> getMainBoard() {
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
        //ArrayList<SpecialCardDeck> specCardDecks = scds.initialize();
        
        MainBoard mb = new MainBoard();
        mb.setCardDeck(cardDecks);
        //mb.setSpecialCardDecks(specCardDecks);
        


        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = 1; i <= 9 ; i++) {
            Card a = cs.getById(i);
            System.out.println(a);
            cards.add(a);
        }
        mb.setCards(cards);
        saveMainBoard(mb);
        System.out.println(cards);
        
        return mb;
    }
    @Transactional
    public MainBoard numberOfSpecialCards(@Valid MainBoard mb, @Valid SpecialCardDeck sc) {
        if (mb.getSpecialCardDecks().size()== 3) {
           return mb; 
        }else{
            SpecialCard lastSpecialCard = sc.getLastSpecialCard();
        for (SpecialCardDeck specialCardDeck : mb.getSpecialCardDecks()) {
            specialCardDeck.getSpecialCards().add(lastSpecialCard);
        
        }
        return mb;
        }
    }
}

