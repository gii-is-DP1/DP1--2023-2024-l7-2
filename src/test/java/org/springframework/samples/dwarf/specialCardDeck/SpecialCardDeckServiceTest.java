package org.springframework.samples.dwarf.specialCardDeck;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.SpecialCard;
import org.springframework.samples.dwarf.card.SpecialCardRepository;
import org.springframework.samples.dwarf.card.SpecialCardService;
import org.springframework.samples.dwarf.exceptions.ResourceNotFoundException;
import org.springframework.samples.dwarf.specialCardDeck.SpecialCardDeck;
import org.springframework.samples.dwarf.specialCardDeck.SpecialCardDeckRepository;
import org.springframework.samples.dwarf.specialCardDeck.SpecialCardDeckService;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

public class SpecialCardDeckServiceTest {

    @Mock
    private SpecialCardDeckRepository specialCardDeckRepository;

    @Mock
    private SpecialCardService specialCardService;

    @InjectMocks
    private SpecialCardDeckService specialCardDeckService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAll() {
        List<SpecialCardDeck> testSpecialCardDecks = new ArrayList<>();
        when(specialCardDeckRepository.findAll()).thenReturn(testSpecialCardDecks);

        List<SpecialCardDeck> result = specialCardDeckService.findAll();

        assertNotNull(result);
        assertEquals(testSpecialCardDecks, result);
    }

    @Test
    public void testGetSpecialCardDeckById() {
        int specialCardDeckId = 1;
        SpecialCardDeck testSpecialCardDeck = new SpecialCardDeck();

        when(specialCardDeckRepository.findById(specialCardDeckId)).thenReturn(Optional.of(testSpecialCardDeck));

        SpecialCardDeck result = specialCardDeckService.getSpecialCardDeckById(specialCardDeckId);

        assertNotNull(result);
        assertEquals(testSpecialCardDeck, result);

        int nonExistentSpecialCardDeckId = 2;
        when(specialCardDeckRepository.findById(nonExistentSpecialCardDeckId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> specialCardDeckService.getSpecialCardDeckById(nonExistentSpecialCardDeckId));
    }

    @Test
    public void testInitializeOneCardDeck() {
        ArrayList<SpecialCard> testCards = new ArrayList<>();
        testCards.add(new SpecialCard());

        SpecialCardDeck result = specialCardDeckService.initializeOneCardDeck(testCards);

        assertNotNull(result);
        assertEquals(testCards.get(0), result.getLastSpecialCard());
        assertEquals(testCards, result.getSpecialCards());

        verify(specialCardDeckRepository, times(1)).save(any());
    }

    @Test
    public void testSaveSpecialCardDeck() {
        SpecialCardDeck testSpecialCardDeck = new SpecialCardDeck();

        when(specialCardDeckRepository.save(testSpecialCardDeck)).thenReturn(testSpecialCardDeck);

        SpecialCardDeck result = specialCardDeckService.saveSpecialCardDeck(testSpecialCardDeck);

        assertNotNull(result);
        assertEquals(testSpecialCardDeck, result);
    }

}
