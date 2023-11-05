package org.springframework.samples.petclinic.cardDeck;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class CardDeckService {

    CardDeckRepository cdr;

    @Autowired
    public CardDeckService(CardDeckRepository cdr) {
        this.cdr = cdr;
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
    public CardDeck updateCardDeck(@Valid CardDeck cd, int cardDeckId) {
        return cdr.updateCardDeck(cd, cardDeckId);
    }

    @Transactional(readOnly = true)
    public List<Card> getTwoCards(Integer id) {

        List<Card> cards = getCardDeckById(id).getCards();

        Collections.shuffle(cards);

        Card lastCard = getCardDeckById(id).getLastCard();
        Integer lastCardIndex = cards.indexOf(lastCard);

        List<Card> twoCards = List.of(cards.get(lastCardIndex));
        while (twoCards.size() < 2 && lastCardIndex < cards.size() - 1) {
            if (cards.get(lastCardIndex).getPosition() != cards.get(lastCardIndex + 1).getPosition())
                twoCards.add(cards.get(lastCardIndex + 1));
            lastCardIndex++;
        }

        CardDeck newCd = getCardDeckById(id);
        newCd.setLastCard(cards.get(lastCardIndex + 2));
        updateCardDeck(newCd, id);

        return twoCards;
    }

}