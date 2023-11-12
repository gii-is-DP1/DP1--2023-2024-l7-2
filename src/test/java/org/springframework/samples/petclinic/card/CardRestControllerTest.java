package org.springframework.samples.petclinic.card;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CardRestControllerTest {

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardRestController cardController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAll() {
        List<Card> testCards = new ArrayList<>();
        when(cardService.getCards()).thenReturn(testCards);
        ResponseEntity<List<Card>> response = cardController.findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testCards, response.getBody());
        verify(cardService, times(1)).getCards();
    }

    @Test
    public void testFindCard() {
        Card testCard = new Card();
        when(cardService.getById(1)).thenReturn(testCard);

       
        ResponseEntity<Card> response = cardController.findCard(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testCard, response.getBody());

        
        verify(cardService, times(1)).getById(1);

        
        when(cardService.getById(2)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> cardController.findCard(2));
    }

}
