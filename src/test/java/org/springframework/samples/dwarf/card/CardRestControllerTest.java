package org.springframework.samples.dwarf.card;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.dwarf.exceptions.BadRequestException;
import org.springframework.samples.dwarf.exceptions.ResourceNotFoundException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

class CardRestControllerTest {

    private CardService cardService = mock(CardService.class);
    private CardRestController cardRestController = new CardRestController(cardService);

    @BeforeEach
    void setUp() {
        
        reset(cardService);
    }

    @Test
    void findAll_ReturnsListOfCards() {
       
        List<Card> cards = new ArrayList<>();
        when(cardService.getCards()).thenReturn(cards);

        
        ResponseEntity<List<Card>> responseEntity = cardRestController.findAll();

        
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(cards, responseEntity.getBody());
        verify(cardService, times(1)).getCards();
    }

    @Test
    void findCard_ExistingCard_ReturnsCard() {
        
        int cardId = 1;
        Card existingCard = new Card();
        when(cardService.getById(cardId)).thenReturn(existingCard);

        
        ResponseEntity<Card> responseEntity = cardRestController.findCard(cardId);

        
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(existingCard, responseEntity.getBody());
        verify(cardService, times(1)).getById(cardId);
    }


    @Test
    void createCard_ValidCard() {
        
        Card validCard = new Card();
        validCard.setName("a");
        validCard.setId(1);
        BindingResult bindingResult = new BeanPropertyBindingResult(validCard, "validCard");

        
        ResponseEntity<Card> responseEntity = cardRestController.createCard(validCard, bindingResult);

        
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(cardService, times(1)).saveCard(validCard);
    }

    @Test
    void findCard_NonExistingCard() {
        
        int nonExistingCardId = 999;
        when(cardService.getById(nonExistingCardId)).thenReturn(null);

        
        assertThrows(ResourceNotFoundException.class, () -> cardRestController.findCard(nonExistingCardId));
        verify(cardService, times(1)).getById(nonExistingCardId);
    }

    @Test
    void createCard_InvalidCard_ThrowsBadRequestException() {
        
        Card invalidCard = new Card();
        BindingResult bindingResult = new BeanPropertyBindingResult(invalidCard, "invalidCard");
        bindingResult.reject("fieldErrorCode", "Error message");

       
        assertThrows(BadRequestException.class, () -> cardRestController.createCard(invalidCard, bindingResult));
        verify(cardService, never()).saveCard(invalidCard);
    }

    

}

