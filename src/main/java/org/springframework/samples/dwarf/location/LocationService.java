package org.springframework.samples.dwarf.location;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.dwarf.card.Card;
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
    public Location getById(Integer id){
        return repo.findById(id).get();
    }

    @Transactional
    public Location save(@Valid Location location) {
        return repo.save(location);
    }

    public Location pushCard(@Valid Location location, Card c) {
        List<Card> locationCards = location.getCards();
        locationCards.add(c);
        location.setCards(locationCards);
        return save(location);
    }
}
