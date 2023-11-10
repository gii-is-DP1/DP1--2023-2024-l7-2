package org.springframework.samples.petclinic.cardDeck;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.CardRepository;
import org.springframework.samples.petclinic.exceptions.ResourceNotFoundException;
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
    public CardDeck initialiate() throws DataAccessException {
        // Not tested
        CardDeck cd = new CardDeck();

        List<Card> cards = cardrepo.findAll();
        Collections.shuffle(cards);

        cd.setCards(cards);
        cd.setLastCard(cards.get(0));

        cdr.save(cd);
        return cd;
    }

    @Transactional
    public CardDeck saveCardDeck(@Valid CardDeck cd) throws DataAccessException {
        cdr.save(cd);

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
        /* 
        BeanUtils.copyProperties(cd, carddeck);
        return saveCardDeck(carddeck);*/
    }

    @Transactional
    public List<Card> getTwoCards(Integer id) {

        /*
         * Al inicio de cada ronda se robarán dos cartas del mazo y 
         * se colocarán en la posición indicada. Si las dos cartas robadas 
         * se deben colocarse en el mismo lugar, 
         * se robará una tercera carta, si esta tercera carta 
         * robada se debe colocar en el mismo lugar, no se robará una cuarta carta.
         * 
         */

        List<Card> cards = getCardDeckById(id).getCards();

        Card lastCard = getCardDeckById(id).getLastCard();
        Integer lastCardIndex = cards.indexOf(lastCard);

        List<Card> twoCards = List.of();

        Card firstCard = cards.get(lastCardIndex);
        Card secondCard = cards.get(lastCardIndex + 1);
        Integer offset = 2;
        if (firstCard.getPosition().equals(secondCard.getPosition())) {
            secondCard = cards.get(lastCardIndex + 2);
            offset++;

            if (firstCard.getPosition().equals(secondCard.getPosition())) {
                twoCards = List.of(firstCard);
            } else {
                twoCards = List.of(firstCard,secondCard);
            }

        } else {
                twoCards = List.of(firstCard,secondCard);
        }


        /* 
        while (twoCards.size() < 2 && lastCardIndex < cards.size() - 1) {
            if (cards.get(lastCardIndex).getPosition() != cards.get(lastCardIndex + 1).getPosition())
                twoCards.add(cards.get(lastCardIndex + 1));
            lastCardIndex++;
        }
        */

        CardDeck newCd = getCardDeckById(id);
        newCd.setLastCard(cards.get(lastCardIndex + offset));
        saveCardDeck(newCd);

        return twoCards;
    }

}