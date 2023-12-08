package org.springframework.samples.dwarf.specialCardDeck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.dwarf.card.SpecialCard;
import org.springframework.samples.dwarf.exceptions.BadRequestException;
import org.springframework.samples.dwarf.exceptions.ResourceNotFoundException;
import org.springframework.validation.BindingResult;

public class SpecialCardDeckControllerTest {

    @Mock
    private SpecialCardDeckService specialCardDeckService;

    @InjectMocks
    private SpecialCardDeckController specialCardDeckController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        List<SpecialCardDeck> testSpecialCardDecks = new ArrayList<>();
        when(specialCardDeckService.findAll()).thenReturn(testSpecialCardDecks);

        ResponseEntity<List<SpecialCardDeck>> response = specialCardDeckController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testSpecialCardDecks, response.getBody());
        verify(specialCardDeckService, times(1)).findAll();
    }

    @Test
    public void testGetSpecialCardDeckById() {
        int specialCardDeckId = 1;
        SpecialCardDeck testSpecialCardDeck = new SpecialCardDeck();

        when(specialCardDeckService.getSpecialCardDeckById(specialCardDeckId)).thenReturn(testSpecialCardDeck);

        ResponseEntity<SpecialCardDeck> response = specialCardDeckController.getSpecialCardDeckById(specialCardDeckId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testSpecialCardDeck, response.getBody());
        verify(specialCardDeckService, times(1)).getSpecialCardDeckById(specialCardDeckId);

        int nonExistentSpecialCardDeckId = 2;
        when(specialCardDeckService.getSpecialCardDeckById(nonExistentSpecialCardDeckId)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> specialCardDeckController.getSpecialCardDeckById(nonExistentSpecialCardDeckId));
    }

    @Test
    public void testGetSpecialCard() {
        int specialCardId = 1;
        SpecialCard testSpecialCard = new SpecialCard();

        when(specialCardDeckService.getSpecialCard(specialCardId)).thenReturn(testSpecialCard);

        ResponseEntity<SpecialCard> response = specialCardDeckController.getSpecialCard(specialCardId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testSpecialCard, response.getBody());
        verify(specialCardDeckService, times(1)).getSpecialCard(specialCardId);

        int nonExistentSpecialCardId = 2;
        when(specialCardDeckService.getSpecialCard(nonExistentSpecialCardId)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> specialCardDeckController.getSpecialCard(nonExistentSpecialCardId));
    }

    @Test
    public void testCreateSpecialCardDeck() {
        SpecialCardDeck newSpecialCardDeck = new SpecialCardDeck();
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(specialCardDeckService.saveSpecialCardDeck(newSpecialCardDeck)).thenReturn(newSpecialCardDeck);

        ResponseEntity<SpecialCardDeck> response = specialCardDeckController.createSpecialCardDeck(newSpecialCardDeck,
                bindingResult);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newSpecialCardDeck, response.getBody());
        verify(specialCardDeckService, times(1)).saveSpecialCardDeck(newSpecialCardDeck);

        when(bindingResult.hasErrors()).thenReturn(true);
        assertThrows(BadRequestException.class,
                () -> specialCardDeckController.createSpecialCardDeck(newSpecialCardDeck, bindingResult));
    }

    @Test
    public void testUpdateSpecialCardDeck() {
        int specialCardDeckId = 1;
        SpecialCardDeck updatedSpecialCardDeck = new SpecialCardDeck();
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(specialCardDeckService.updateSpecialCardDeck(updatedSpecialCardDeck, specialCardDeckId))
                .thenReturn(updatedSpecialCardDeck);

        ResponseEntity<SpecialCardDeck> response = specialCardDeckController.updateSpecialCardDeck(specialCardDeckId,
                updatedSpecialCardDeck, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedSpecialCardDeck, response.getBody());
        verify(specialCardDeckService, times(1)).updateSpecialCardDeck(updatedSpecialCardDeck, specialCardDeckId);

        when(bindingResult.hasErrors()).thenReturn(true);
        assertThrows(BadRequestException.class, () -> specialCardDeckController.updateSpecialCardDeck(specialCardDeckId,
                updatedSpecialCardDeck, bindingResult));
    }
}
