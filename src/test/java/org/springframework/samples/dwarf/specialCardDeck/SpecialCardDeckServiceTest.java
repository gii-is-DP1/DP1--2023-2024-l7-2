package org.springframework.samples.dwarf.specialCardDeck;

import static org.junit.jupiter.api.Assertions.*;
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
import org.springframework.dao.DataAccessException;
import org.springframework.samples.dwarf.card.SpecialCard;
import org.springframework.samples.dwarf.card.SpecialCardRepository;
import org.springframework.samples.dwarf.card.SpecialCardService;
import org.springframework.samples.dwarf.cardDeck.CardDeck;
import org.springframework.samples.dwarf.exceptions.ResourceNotFoundException;

@SpringBootTest
public class SpecialCardDeckServiceTest {

    @Mock
    private SpecialCardDeckRepository specialCardDeckRepository;

    @Mock
    private SpecialCardService specialCardService;
    @Mock
    private SpecialCardRepository specialCardRepository;

    @InjectMocks
    private SpecialCardDeckService specialCardDeckService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Tests findAll()
    // ----------------------------------------------------------------------------------------------------
    @Test
    public void testFindAll() {

        List<SpecialCardDeck> expectedDecks = new ArrayList<>();
        expectedDecks.add(new SpecialCardDeck());

        when(specialCardDeckRepository.findAll()).thenReturn(expectedDecks);

        List<SpecialCardDeck> result = specialCardDeckService.findAll();

        assertNotNull(result);
        assertEquals(expectedDecks, result);

    }

    @Test
    public void testFindAll_emptyList() {

        List<SpecialCardDeck> expectedDecks = new ArrayList<>();

        when(specialCardDeckRepository.findAll()).thenReturn(expectedDecks);

        List<SpecialCardDeck> result = specialCardDeckService.findAll();

        assertNotNull(result);
        assertEquals(0, result.size());

    }

    // Tests getSpecialCardDeckById(Integer specialCardDeckId)
    // -----------------------------------------------------------
    @Test
    public void testGetSpecialCardDeckById_validId() {

        SpecialCardDeck expectedDeck = new SpecialCardDeck();

        when(specialCardDeckRepository.findById(1)).thenReturn(Optional.of(expectedDeck));

        SpecialCardDeck result = specialCardDeckService.getSpecialCardDeckById(1);

        assertEquals(expectedDeck, result);

    }

    @Test
    public void testGetSpecialCardDeckById_invalidId() {

        when(specialCardDeckRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            specialCardDeckService.getSpecialCardDeckById(99);
        });

    }

    @Test
    public void testGetSpecialCardDeckById_nullCheck() {

        when(specialCardDeckRepository.findById(1)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> {
            specialCardDeckService.getSpecialCardDeckById(1);
        });

    }

    // Tests initialize()
    // --------------------------------------------------------------------------------------------
    @Test
    void testInitialize_valid() {

        List<SpecialCard> testCards = new ArrayList<>();

        when(specialCardService.getSpecialCards()).thenReturn(testCards);

        SpecialCardDeck result = specialCardDeckService.initialize();

        assertNotNull(result);
        assertEquals(testCards.size() - 3, result.getSpecialCards().size());
        assertEquals(testCards.get(testCards.size() - 3), result.getLastSpecialCard());

    }

    @Test
    void testInitialize_noCards() {

        when(specialCardService.getSpecialCards()).thenReturn(Collections.emptyList());

        SpecialCardDeck result = specialCardDeckService.initialize();

        assertNotNull(result);
        assertTrue(result.getSpecialCards().isEmpty());
        assertNull(result.getLastSpecialCard());
    }

    @Test
    void testInitialize_dataAccessException() {

        when(specialCardService.getSpecialCards()).thenThrow(new DataAccessException("") {
        });

        assertThrows(DataAccessException.class, () -> {
            specialCardDeckService.initialize();
        });
    }

    // Tests getSpecialCard(Integer specialCardDeckId)
    // -----------------------------------------------------------------------------
    @Test
    void testGetSpecialCard_valid() {
        SpecialCardDeck expectedDeck = new SpecialCardDeck();

        when(specialCardDeckRepository.findById(1)).thenReturn(Optional.of(expectedDeck));

        SpecialCardDeck result = specialCardDeckRepository.findById(1).get();

        assertEquals(expectedDeck, result);
    }

    @Test
    void testGetSpecialCard_invalidId() {

        assertThrows(ResourceNotFoundException.class, () -> {
            specialCardDeckService.getSpecialCard(99);
        });
    }

    // Tests saveSpecialCardDeck(SpecialCardDeck specialCardDeck)
    // --------------------------------------------------------------------
    @Test
    void testSaveSpecialCardDeck_valid() {
        SpecialCardDeckService service = new SpecialCardDeckService(specialCardDeckRepository, null);
        SpecialCardDeck deck = new SpecialCardDeck();

        SpecialCardDeck result = service.saveSpecialCardDeck(deck);

        assertNotNull(result);
        // Additional asserts to validate deck was saved
    }

    @Test
    void testSaveSpecialCardDeck_null() {
        SpecialCardDeckService service = new SpecialCardDeckService(specialCardDeckRepository, null);

        assertThrows(NullPointerException.class, () -> {
            service.saveSpecialCardDeck(null);
        });
    }

    // Tests updateSpecialCardDeck(SpecialCardDeck specialCardDeck, Integer
    // specialCardDeckId) -----------------------------
    @Test
    void testUpdateSpecialCardDeck_valid() {

        SpecialCardDeck existingDeck = new SpecialCardDeck();
        existingDeck.setId(1);

        SpecialCardDeck updatedDeck = new SpecialCardDeck();
        updatedDeck.setId(1);

        when(specialCardDeckRepository.findById(1)).thenReturn(Optional.of(existingDeck));

        SpecialCardDeck result = specialCardDeckService.updateSpecialCardDeck(updatedDeck, 1);

        assertEquals(updatedDeck, result);
        verify(specialCardDeckRepository).save(updatedDeck);

    }

    @Test
    void testUpdateSpecialCardDeck_nullDeck() {

        SpecialCardDeck existingDeck = new SpecialCardDeck();
        existingDeck.setId(1);

        SpecialCardDeck updatedDeck = null;

        when(specialCardDeckRepository.findById(1)).thenReturn(Optional.of(existingDeck));

        SpecialCardDeck result = specialCardDeckService.updateSpecialCardDeck(updatedDeck, 1);

        assertEquals(updatedDeck, result);
        assertNull(result);
        verify(specialCardDeckRepository).save(updatedDeck);

    }

}
