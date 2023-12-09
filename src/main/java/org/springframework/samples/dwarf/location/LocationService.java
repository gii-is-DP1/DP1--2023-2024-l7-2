package org.springframework.samples.dwarf.location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.player.Player;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class LocationService {
    
    LocationRepository repo;

    @Autowired
    public LocationService(LocationRepository repo) {
        this.repo = repo;
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
}
