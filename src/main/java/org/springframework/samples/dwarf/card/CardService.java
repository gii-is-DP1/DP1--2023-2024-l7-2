package org.springframework.samples.dwarf.card;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class CardService {

    CardRepository repo;

    @Autowired
    public CardService(CardRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Card> getCards() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Card getById(int id) {
        Optional<Card> result = repo.findById(id);
        return result.isPresent() ? result.get() : null;
    }

    @Transactional
    public Card saveCard(@Valid Card newCard) {
        return repo.save(newCard);
    }

    @Transactional
    public void deleteCardById(int id) {
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Card getCardByName(String name) {
        return repo.findByName(name).get(0);
    }
}
