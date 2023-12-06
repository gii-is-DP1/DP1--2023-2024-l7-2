package org.springframework.samples.dwarf.MainBoard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.cardDeck.CardDeck;
import org.springframework.samples.dwarf.mainboard.MainBoard;
import org.springframework.samples.dwarf.specialCardDeck.SpecialCardDeck;

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
    public void testGetAndSetSpecialCardDeck() {
        SpecialCardDeck specialCardDeck = mock(SpecialCardDeck.class);

        mainBoard.setSpecialCardDeck(specialCardDeck);

        assertEquals(specialCardDeck, mainBoard.getSpecialCardDeck());
    }

    @Test
    public void testGetAndSetCards() {
        Card card1 = mock(Card.class);
        Card card2 = mock(Card.class);
        List<Card> cards = Arrays.asList(card1, card2);

        //mainBoard.setCards(cards);

        assertEquals(cards, mainBoard.getCards());
    }

    @Test
    public void testEntityId() {
        Integer id = 1;
        mainBoard.setId(id);

        assertEquals(id, mainBoard.getId());
    }

}
