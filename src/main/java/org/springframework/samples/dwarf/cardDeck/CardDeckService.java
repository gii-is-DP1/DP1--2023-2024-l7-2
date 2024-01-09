package org.springframework.samples.dwarf.cardDeck;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.CardRepository;
import org.springframework.samples.dwarf.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class CardDeckService {

    CardDeckRepository cdr;
    CardRepository cardrepo;

    @Autowired
    public CardDeckService(CardDeckRepository cdr, CardRepository cardrepo) {
        this.cdr = cdr;
        this.cardrepo = cardrepo;
    }

    @Transactional(readOnly = true)
    public List<CardDeck> findAll() {
        return (List<CardDeck>) cdr.findAll();
    }

    @Transactional(readOnly = true)
    public CardDeck getCardDeckById(Integer id) {
        return cdr.findById(id).orElseThrow(() -> new ResourceNotFoundException("CardDeck", "Id", id));
    }

    @Transactional
    public CardDeck shuffleAndSaveCards(CardDeck cd, List<Card> cards) {

        Collections.shuffle(cards);
        cd.setCards(cards);
        return saveCardDeck(cd);
    }

    @Transactional
    public CardDeck initialiate() throws DataAccessException {
        // Not tested
        CardDeck cd = new CardDeck();

        ArrayList<Card> cards = new ArrayList<Card>();
        cards.addAll(cardrepo.findAll());

        // TODO: test this
        for (int i = 1; i < 10; i++) {
            cards.remove(0);
        }

        cd = shuffleAndSaveCards(cd, cards);
        return cd;
    }

    @Transactional
    public CardDeck saveCardDeck(@Valid CardDeck cd) throws DataAccessException {
        if (cd.equals(null)) {
            throw new NullPointerException();
        } else {
            cdr.save(cd);
        }
        return cd;
    }

    @Transactional
    public CardDeck updateCardDeck(@Valid CardDeck cd, int cardDeckId) {
        CardDeck carddeck = getCardDeckById(cardDeckId);

        if (carddeck != null) {
            saveCardDeck(cd);
            return cd;
        } else {
            return null;
        }
    }

    @Transactional
    public List<Card> getNewCards(Integer id) {

        /*
         * Al inicio de cada ronda se robarán dos cartas del mazo y
         * se colocarán en la posición indicada. Si las dos cartas robadas
         * se deben colocarse en el mismo lugar,
         * se robará una tercera carta, si esta tercera carta
         * robada se debe colocar en el mismo lugar, no se robará una cuarta carta.
         * 
         */

        CardDeck cd = getCardDeckById(id);

        List<Card> cards = cd.getCards();

        ArrayList<Card> newCards = new ArrayList<>();

        Integer offset = 0;
        Card firstCard = cards.get(offset);
        offset++;
        Card secondCard = cards.get(offset);
        offset++;
        newCards.add(firstCard);
        // Integer offset = 2;
        if (firstCard.getPosition().equals(secondCard.getPosition())) {
            secondCard = cards.get(offset);
            offset++;

            if (!firstCard.getPosition().equals(secondCard.getPosition())) {

                newCards.add(secondCard);
            }
        } else {
            newCards.add(secondCard);
        }

        cards.removeAll(newCards);
        cd.setCards(cards);

        saveCardDeck(cd);

        return newCards;
    }

}