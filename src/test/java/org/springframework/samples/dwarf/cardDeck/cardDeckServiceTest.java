package org.springframework.samples.dwarf.cardDeck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.cardDeck.CardDeck;
import org.springframework.samples.dwarf.cardDeck.CardDeckService;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class cardDeckServiceTest {

    @Autowired
    private CardDeckService cardDeckService;

    @Test
    public void testFindAll() {
        List<CardDeck> cardDecks = cardDeckService.findAll();
        assertNotNull(cardDecks);
        // Asegúrate de ajustar las aserciones según la lógica de tu aplicación.
    }

    @Test
    public void testGetCardDeckById() {
        CardDeck existingCardDeck = cardDeckService.initialiate();
        Integer cardDeckId = existingCardDeck.getId();

        CardDeck retrievedCardDeck = cardDeckService.getCardDeckById(cardDeckId);
        assertNotNull(retrievedCardDeck);
        assertEquals(cardDeckId, retrievedCardDeck.getId());
        // Asegúrate de ajustar las aserciones según la lógica de tu aplicación.
    }

    @Test
    public void testInitialiate() {
        CardDeck initializedCardDeck = cardDeckService.initialiate();
        assertNotNull(initializedCardDeck);
        // Asegúrate de ajustar las aserciones según la lógica de tu aplicación.
    }

    @Test
    public void testSaveCardDeck() {
        CardDeck newCardDeck = new CardDeck();

        CardDeck savedCardDeck = cardDeckService.saveCardDeck(newCardDeck);

        assertNotNull(savedCardDeck.getId());
        // Asegúrate de ajustar las aserciones según la lógica de tu aplicación.
    }

    @Test
    public void testUpdateCardDeck() {
        CardDeck existingCardDeck = cardDeckService.initialiate();
        int cardDeckId = existingCardDeck.getId();

        CardDeck updatedCardDeck = cardDeckService.updateCardDeck(existingCardDeck, cardDeckId);
        assertNotNull(updatedCardDeck);
        assertEquals(cardDeckId, updatedCardDeck.getId());
        // Asegúrate de ajustar las aserciones según la lógica de tu aplicación.
    }

    @Test
    public void testGetTwoCards() {
        CardDeck existingCardDeck = cardDeckService.initialiate();
        Integer cardDeckId = existingCardDeck.getId();

        List<Card> twoCards = cardDeckService.getTwoCards(cardDeckId);
        assertNotNull(twoCards);
        // Asegúrate de ajustar las aserciones según la lógica de tu aplicación.
    }
}
