package org.springframework.samples.petclinic.cardDeck;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.exceptions.BadRequestException;
import org.springframework.samples.petclinic.exceptions.ResourceNotFoundException;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class cardDeckRestControllerTest {

    @Mock
    private CardDeckService cardDeckService;

    @InjectMocks
    private CardDeckController cardDeckController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAll() {
        List<CardDeck> testCardDecks = new ArrayList<>();
        when(cardDeckService.findAll()).thenReturn(testCardDecks);
        ResponseEntity<List<CardDeck>> response = cardDeckController.findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testCardDecks, response.getBody());
        verify(cardDeckService, times(1)).findAll();
    }

    @Test
    public void testGetCardDeckById() {
        int cardDeckId = 1;
        CardDeck testCardDeck = new CardDeck();

        when(cardDeckService.getCardDeckById(cardDeckId)).thenReturn(testCardDeck);

        ResponseEntity<CardDeck> response = cardDeckController.getCardDeckById(cardDeckId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testCardDeck, response.getBody());
        verify(cardDeckService, times(1)).getCardDeckById(cardDeckId);

        int nonExistentCardDeckId = 15;
        when(cardDeckService.getCardDeckById(nonExistentCardDeckId)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> {
    cardDeckController.getCardDeckById(nonExistentCardDeckId);
});
    }

    @Test
    public void testCreateCardDeck() {
        CardDeck newCardDeck = new CardDeck();
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(cardDeckService.saveCardDeck(newCardDeck)).thenReturn(newCardDeck);

        ResponseEntity<CardDeck> response = cardDeckController.createCardDeck(newCardDeck, bindingResult);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newCardDeck, response.getBody());
        verify(cardDeckService, times(1)).saveCardDeck(newCardDeck);

        when(bindingResult.hasErrors()).thenReturn(true);
        assertThrows(BadRequestException.class, () -> cardDeckController.createCardDeck(newCardDeck, bindingResult));
    }

    @Test
    public void testUpdateCardDeck() {
        int cardDeckId = 1;
        CardDeck updatedCardDeck = new CardDeck();
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(cardDeckService.updateCardDeck(updatedCardDeck, cardDeckId)).thenReturn(updatedCardDeck);

        ResponseEntity<CardDeck> response = cardDeckController.updateCardDeck(cardDeckId, updatedCardDeck, bindingResult);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCardDeck, response.getBody());
        verify(cardDeckService, times(1)).updateCardDeck(updatedCardDeck, cardDeckId);

        when(bindingResult.hasErrors()).thenReturn(true);
        assertThrows(BadRequestException.class, () -> cardDeckController.updateCardDeck(cardDeckId, updatedCardDeck, bindingResult));
    }
}
