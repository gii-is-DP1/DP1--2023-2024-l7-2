package org.springframework.samples.dwarf.MainBoard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.CardService;
import org.springframework.samples.dwarf.cardDeck.CardDeck;
import org.springframework.samples.dwarf.location.Location;
import org.springframework.samples.dwarf.mainboard.MainBoard;

public class MainBoardTest {

    private MainBoard mainBoard;

    @Mock
    private CardService cs;

    @BeforeEach
    public void setUp() {
        cs = mock(CardService.class);
        mainBoard = new MainBoard();
    }

    @Test
    public void testGetAndSetCardDeck() {
        CardDeck cardDeck = mock(CardDeck.class);

        mainBoard.setCardDeck(cardDeck);

        assertEquals(cardDeck, mainBoard.getCardDeck());
    }

    @Test
    public void testGetCards_Positive() {
        // Arrange
        MainBoard mainBoard = new MainBoard();

        Card card = cs.getById(4);
        List<Card> cards = new ArrayList();
        cards.add(card);

        Location location = new Location();
        List<Location> locations = new ArrayList();
        locations.add(location);

        location.setCards(cards);
        mainBoard.setLocations(locations);

        // Act
        List<Card> cardsMainBoard = mainBoard.getCards();

        // Assert
        assertNotNull(cardsMainBoard);
        assertFalse(cardsMainBoard.isEmpty());
        assertEquals(card, cards.get(0));
    }

    @Test
    public void testGetCards_EmptyLocations_Negative() {
        // Arrange
        MainBoard mainBoard = new MainBoard();
        List<Location> locations = new ArrayList();
        mainBoard.setLocations(locations);

        // Act
        List<Card> cards = mainBoard.getCards();

        // Assert
        assertTrue(cards.isEmpty());
    }

    @Test
    public void testGetLocationCards_Positive() {
        // Arrange
        MainBoard mainBoard = new MainBoard();

        Card card = cs.getById(4);
        List<Card> cards = new ArrayList();
        cards.add(card);

        Location location = new Location();
        List<Location> locations = new ArrayList();
        locations.add(location);

        location.setCards(cards);
        mainBoard.setLocations(locations);

        // Act
        List<Card> cardsMainBoard = mainBoard.getLocationCards(mainBoard.getLocations());

        // Assert
        assertNotNull(cardsMainBoard);
        assertFalse(cardsMainBoard.isEmpty());
        assertEquals(card, cardsMainBoard.get(0));
    }

    @Test
    public void testEntityId() {
        Integer id = 1;
        mainBoard.setId(id);

        assertEquals(id, mainBoard.getId());
    }

}
