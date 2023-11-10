package org.springframework.samples.petclinic.mainboard;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
    
    @Autowired
    public MainBoardService(MainBoardRepository repo, CardDeckService cds, SpecialCardDeckService scds) {
        this.repo = repo;
        this.cds = cds;
        this.scds = scds;
    }

    @Transactional(readOnly = true)
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
        List<SpecialCardDeck> specCardDecks = scds.initialize();
        
        MainBoard mb = new MainBoard();
        mb.setCardDeck(cardDecks);
        mb.setSpecialCardDecks(specCardDecks);
        saveMainBoard(mb);

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

