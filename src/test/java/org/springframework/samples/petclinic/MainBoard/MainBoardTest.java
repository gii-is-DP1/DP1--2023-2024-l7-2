package org.springframework.samples.petclinic.MainBoard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.cardDeck.CardDeck;
import org.springframework.samples.petclinic.mainboard.MainBoard;
import org.springframework.samples.petclinic.specialCardDeck.SpecialCardDeck;
import org.springframework.samples.petclinic.card.Card;

public class MainBoardTest {

    private MainBoard mainBoard;

    @BeforeEach
    public void setUp() {
        mainBoard = new MainBoard();
    }

    @Test
    public void testGetAndSetCardDeck() {
        CardDeck cardDeck = mock(CardDeck.class);

        mainBoard.setCardDeck(cardDeck);

        assertEquals(cardDeck, mainBoard.getCardDeck());
    }

    @Test
    public void testGetAndSetSpecialCardDecks() {
        SpecialCardDeck specialCardDeck1 = mock(SpecialCardDeck.class);
        SpecialCardDeck specialCardDeck2 = mock(SpecialCardDeck.class);
        List<SpecialCardDeck> specialCardDecks = Arrays.asList(specialCardDeck1, specialCardDeck2);

        mainBoard.setSpecialCardDecks(specialCardDecks);

        assertEquals(specialCardDecks, mainBoard.getSpecialCardDecks());
    }

    @Test
    public void testGetAndSetCards() {
        Card card1 = mock(Card.class);
        Card card2 = mock(Card.class);
        List<Card> cards = Arrays.asList(card1, card2);

        mainBoard.setCards(cards);

        assertEquals(cards, mainBoard.getCards());
    }

    @Test
    public void testEntityId() {
        Integer id = 1;
        mainBoard.setId(id);

        assertEquals(id, mainBoard.getId());
    }

    
}
