package org.springframework.samples.dwarf.cardDeck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.cardDeck.CardDeck;

@SpringBootTest
public class cardDeckTest {

    @Test
    public void testCreateCardDeck() {
        CardDeck cardDeck = new CardDeck();
        assertNotNull(cardDeck);
    }

    @Test
    public void testCardDeckProperties() {
        CardDeck cardDeck = new CardDeck();

        // Configura el objeto CardType según tus necesidades
        List<Card> cards = new ArrayList<>();
        Card card1 = new Card();
        Card card2 = new Card();
        cards.add(card1);
        cards.add(card2);

        cardDeck.setCards(cards);

        Card lastCard = new Card();
        // Configura el objeto Card según tus necesidades
        cardDeck.setLastCard(lastCard);

        // Asegúrate de que las propiedades se hayan establecido correctamente
        assertEquals(cards, cardDeck.getCards());
        assertEquals(lastCard, cardDeck.getLastCard());
    }

    @Test
    public void testCardAssociation() {
        CardDeck cardDeck = new CardDeck();
        assertNull(cardDeck.getLastCard());

        Card card = new Card();
        cardDeck.setLastCard(card);

        assertNotNull(cardDeck.getLastCard());
        assertEquals(card, cardDeck.getLastCard());
    }
}
