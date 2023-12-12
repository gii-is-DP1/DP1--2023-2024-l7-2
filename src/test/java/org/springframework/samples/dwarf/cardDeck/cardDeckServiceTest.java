package org.springframework.samples.dwarf.cardDeck;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.CardRepository;
import org.springframework.samples.dwarf.card.CardService;
import org.springframework.samples.dwarf.exceptions.ResourceNotFoundException;

@SpringBootTest
public class cardDeckServiceTest {

    @Mock
    private CardDeckRepository cardDeckRepository;

    @Mock
    private CardService cardService;

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardDeckService cardDeckService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Tests findAll()
    // -----------------------------------------------------------------------
    @Test
    void testFindAll_empty() {
        when(cardDeckRepository.findAll()).thenReturn(new ArrayList<>());
        List<CardDeck> result = cardDeckService.findAll();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAll_oneDeck() {
        List<CardDeck> decks = new ArrayList<>();
        decks.add(new CardDeck());
        when(cardDeckRepository.findAll()).thenReturn(decks);
        List<CardDeck> result = cardDeckService.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindAll_multipleDecks() {
        List<CardDeck> decks = new ArrayList<>();
        decks.add(new CardDeck());
        decks.add(new CardDeck());
        when(cardDeckRepository.findAll()).thenReturn(decks);
        List<CardDeck> result = cardDeckService.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // Tests getCardDeckById(Integer cardDeckId)
    // -----------------------------------------------
    @Test
    void testGetCardDeckByIdFound() {
        CardDeck expectedDeck = new CardDeck();

        when(cardDeckRepository.findById(1)).thenReturn(Optional.of(expectedDeck));

        CardDeck result = cardDeckService.getCardDeckById(1);

        assertEquals(expectedDeck, result);
    }

    @Test
    void testGetCardDeckByIdNotFound() {
        CardDeckService service = new CardDeckService(cardDeckRepository, null);
        Integer invalidId = -1;

        assertThrows(ResourceNotFoundException.class, () -> {
            service.getCardDeckById(invalidId);
        });
    }

    // Tests shuffleAndSaveCards(CardDeck cd, List<Card> cards)
    // ------------------------------------
    @Test
    void testShuffleAndSaveCards_valid() {
        CardDeck testDeck = new CardDeck();
        List<Card> testCards = Collections.singletonList(new Card());

        when(cardDeckRepository.save(any())).thenReturn(testDeck);

        CardDeck result = cardDeckService.shuffleAndSaveCards(testDeck, testCards);

        verify(cardDeckRepository).save(testDeck);
        assertEquals(testCards, result.getCards());
    }

    @Test
    public void testShuffleAndSaveCards_nullCards() {
        assertThrows(NullPointerException.class, () -> {
            cardDeckService.shuffleAndSaveCards(new CardDeck(), null);
        });
    }

    @Test
    public void testShuffleAndSaveCards_emptyCards() {
        CardDeck result = cardDeckService.shuffleAndSaveCards(new CardDeck(), Collections.emptyList());

        assertNotNull(result);
        assertEquals(0, result.getCards().size());
    }

    // Test initialize()
    // -----------------------------------------------------------------------
    @Test
    public void testInitialize() {
        // Arrange
        List<Card> testCards = cardRepository.findAll();
        when(cardService.getCards()).thenReturn(testCards);
    
        // Ensure that testCards has at least 9 cards
        assertTrue(testCards.size() <= 9);
    
        // Act
        CardDeck result = cardDeckService.initialiate();
    
        // Assert
        assertEquals(testCards.size() - 9, result.getCards().size());
    }

    // Tests saveCardDeck(CardDeck cardDeck)
    // -----------------------------------------------------
    @Test
    void testSaveCardDeck_valid() {
        CardDeck validCardDeck = new CardDeck();
        CardDeck result = cardDeckService.saveCardDeck(validCardDeck);
        assertNotNull(result);
        assertEquals(validCardDeck, result);
    }

    @Test
    void testSaveCardDeck_invalid() {
        CardDeck invalidCardDeck = null;
        assertThrows(NullPointerException.class, () -> {
            cardDeckService.saveCardDeck(invalidCardDeck);
        });
    }

    // Tessts updateCardDeck(CardDeck cardDeck, Integer cardDeckId)
    // ------------------------------------
    @Test
    void testUpdateCardDeck_valid() {
        CardDeck existingDeck = new CardDeck();
        existingDeck.setId(1);

        CardDeck updatedDeck = new CardDeck();
        updatedDeck.setId(1);

        when(cardDeckRepository.findById(1)).thenReturn(Optional.of(existingDeck));

        CardDeck result = cardDeckService.updateCardDeck(updatedDeck, 1);

        assertEquals(updatedDeck, result);
        verify(cardDeckRepository).save(updatedDeck);
    }

    @Test
    void testUpdateCardDeck_invalid() {
        CardDeck invalidCardDeck = new CardDeck();
        assertThrows(ResourceNotFoundException.class, () -> {
            cardDeckService.updateCardDeck(invalidCardDeck, -1);
        });
    }

    // Tests getNewCards(Integer cardDeckId)
    // --------------------------------------------------------
    @Test
    public void testGetNewCards_ShouldReturnTwoCards() {
        // Arrange
        CardDeck testDeck = new CardDeck();
        testDeck.setId(1);
        testDeck.setCards(cardRepository.findAll());

        when(cardDeckRepository.findById(1)).thenReturn(Optional.of(testDeck));

        // Act
        List<Card> result = cardDeckService.getNewCards(1);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    public void testGetNewCards_ShouldHandleDuplicatePositions() {
        // Arrange
        Card card1 = new Card();
        card1.setId(56);
        card1.setDescription("asdasd");
        card1.setPosition(2);

        Card card2 = new Card();
        card2.setId(57);
        card2.setDescription("asdasd");
        card2.setPosition(1);

        CardDeck testDeck = new CardDeck();
        testDeck.setId(2);
        testDeck.setCards(List.of(card1, card2));

        when(cardDeckRepository.findById(2)).thenReturn(Optional.of(testDeck));

        // Act
        List<Card> result = cardDeckService.getNewCards(2);

        // Assert
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getPosition());
        assertNotEquals(1, result.get(1).getPosition());
    }

    @Test
    public void testGetNewCards_ShouldUpdateDeck() {
        // Arrange
        CardDeck testDeck = new CardDeck();
        List<Card> testCards = new ArrayList<>();

        when(cardDeckRepository.findById(1)).thenReturn(Optional.of(testDeck));
        when(cardDeckRepository.save(any())).thenReturn(testDeck);

        // Act
        cardDeckService.getNewCards(1);

        // Assert
        verify(cardDeckRepository).save(testDeck);
    }

}
