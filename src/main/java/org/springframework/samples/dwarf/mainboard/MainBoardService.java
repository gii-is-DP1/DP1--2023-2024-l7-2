package org.springframework.samples.dwarf.mainboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.CardService;
import org.springframework.samples.dwarf.card.SpecialCard;
import org.springframework.samples.dwarf.card.SpecialCardService;
import org.springframework.samples.dwarf.cardDeck.CardDeck;
import org.springframework.samples.dwarf.cardDeck.CardDeckService;
import org.springframework.samples.dwarf.location.Location;
import org.springframework.samples.dwarf.location.LocationService;
import org.springframework.samples.dwarf.specialCardDeck.SpecialCardDeck;
import org.springframework.samples.dwarf.specialCardDeck.SpecialCardDeckService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class MainBoardService {

    private final Integer MAX_POSITION = 9;
    private final Integer MIN_POSITION = 1;
    private final Integer MAX_NUMBER_SPECIAL_CARD_DECK = 9;


    MainBoardRepository repo;
    private final CardDeckService cds;
    private final SpecialCardDeckService scds;
    private final SpecialCardService scs;
    private final CardService cs;
    private final LocationService ls;

    @Autowired
    public MainBoardService(MainBoardRepository repo, CardDeckService cds, SpecialCardDeckService scds,
            SpecialCardService scs, CardService cs, LocationService ls) {
        this.repo = repo;
        this.cds = cds;
        this.scds = scds;
        this.scs = scs;
        this.cs = cs;
        this.ls = ls;
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

        ArrayList<Location> locations = new ArrayList<Location>();
        for (int i = MIN_POSITION; i <= MAX_POSITION; i++) {
            Card initialCard = cs.getById(i);
            
            Location locationI = new Location();
            locationI.setPosition(i);
            locationI.setCards(List.of(initialCard));
            locationI = ls.save(locationI);
            locations.add(locationI);
        }
        mb.setLocations(locations);

        ArrayList<SpecialCard> sCards = new ArrayList<SpecialCard>();
        sCards.addAll(specCardDeck.getSpecialCards());
        mb.setSCards(sCards);
        
        System.out.println(sCards);
        saveMainBoard(mb);

        return mb;
    }

    @Transactional
    public MainBoard holdACouncilAction(MainBoard mb) {
        CardDeck cd = mb.getCardDeck();
        ArrayList<Card> cardsRemovedFromLocations = new ArrayList<>();

        for (Location lc:mb.getLocations()) {
            Card removedCard = ls.removeLastCard(lc);
            cardsRemovedFromLocations.add(removedCard);
        }

        cardsRemovedFromLocations.addAll(cd.getCards());

        cd = cds.shuffleAndSaveCards(cd, cardsRemovedFromLocations);

        return saveMainBoard(mb);
    }

    @Transactional
    public void collapseTheShaftsAction(MainBoard mb) {

        for (Location lc:mb.getLocations()) {
            ls.putFirstCardAtEnd(lc);
        }
    }

    public void runAmokAction(MainBoard mb) {
        for (Location lc:mb.getLocations()) {
            ls.shuffleLocation(lc);
        }
    }
    
}
