package org.springframework.samples.dwarf.location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.CardService;
import org.springframework.samples.dwarf.player.Player;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class LocationService {
    
    private final LocationRepository repo;
    private final CardService cs;

    private final Integer MAX_POSITION = 9;
    private final Integer MIN_POSITION = 1;

    @Autowired
    public LocationService(LocationRepository repo, CardService cs) {
        this.repo = repo;
        this.cs = cs;
    }

    @Transactional(readOnly=true)
    public List<Location> getAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Location getById(int id){
        Optional<Location> result = repo.findById(id);
        return result.isPresent() ? result.get() : null;
    }

    @Transactional
    public Location save( Location location) {
        return repo.save(location);
    }

    @Transactional
    public List<Location> initialize() {
        ArrayList<Location> locations = new ArrayList<Location>();
        for (int i = MIN_POSITION; i <= MAX_POSITION; i++) {
            Card initialCard = cs.getById(i);
            
            Location locationI = new Location();
            locationI.setPosition(i);
            locationI.setCards(List.of(initialCard));
            locationI = save(locationI);
            locations.add(locationI);
        }

        return locations;
    }

    @Transactional
    public Location pushCard(Location location, Card c) {
        List<Card> locationCards = location.getCards();
    
        
        if (locationCards == null) {
            locationCards = new ArrayList<>();
            location.setCards(locationCards);  
        }
    
        locationCards.add(c);
        location.setCards(locationCards);
        return save(location);
    }
    

    @Transactional
    public Location shuffleLocation( Location location) {
        List<Card> locationCards = location.getCards();
        Collections.shuffle(locationCards);
        location.setCards(locationCards);
        return save(location);
    }


    @Transactional
    public Location putFirstCardAtEnd( Location location) {
        List<Card> locationCards = location.getCards();
        Collections.rotate(locationCards,1);
        location.setCards(locationCards);
        return save(location);
    }

    @Transactional
    public Card removeLastCard(Location location) {
        List<Card> locationCards = location.getCards();
        Card toRemove = null;
        if (locationCards.size() > 1){
            toRemove = locationCards.get(locationCards.size() -1);
        }
        locationCards.remove(toRemove);
        location.setCards(locationCards);
        return toRemove;
    }
/* 
    @Transactional
    public List<Card> removeAllCardsExceptOne(Location location) {
        List<Card> res = location.getCards();
        Card remainingCard = res.get(0);
        res.remove(remainingCard);

        location.setCards(List.of(remainingCard));
        save(location);

        return res;
    }
*/
    @Transactional
    public Location pastGloriesAction(Location location, Card c) {
        List<Card> locationCards = location.getCards();
        if (!locationCards.contains(c)) {
            // TODO: Create error
            return null;
        }
        locationCards.remove(c);
        locationCards.add(c);
        location.setCards(locationCards);

        return save(location);
    }

    @Transactional
    public Location pushCards(Location location,List<Card> c) {
        List<Card> locationCards = location.getCards();
    
        
        if (locationCards == null) {
            locationCards = new ArrayList<>();
            location.setCards(locationCards);  
        }
    
        locationCards.addAll(c);
        location.setCards(locationCards);
        return save(location);
    }
    

    @Transactional(readOnly = true)
    public List<Card> getPreviousCards(Location location) {
        List<Card> locationCards = location.getCards();
        
        // Verifica si hay al menos dos cartas en la ubicación
        if (locationCards != null && locationCards.size() >= 2) {
            // Devuelve una lista que contiene todas las cartas anteriores a la posición actual
            return new ArrayList<>(locationCards.subList(0, locationCards.size() - 1));
        } else {
            return Collections.emptyList(); 
        }
    }
}
