package org.springframework.samples.petclinic.card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CardServiceTest {

    @Autowired
    private CardService cardService;

    @Test
    public void testGetCards() {
        List<Card> cards = cardService.getCards();
        assertNotNull(cards);
        
    }

    @Test
    public void testGetById() {
        
        Card existingCard = cardService.getCards().get(0);

        Card retrievedCard = cardService.getById(existingCard.getId());
        assertNotNull(retrievedCard);
        assertEquals(existingCard.getId(), retrievedCard.getId());
      
    }

    @Test
    public void testSaveCard() {
        Card newCard = new Card();
        

        Card savedCard = cardService.saveCard(newCard);

        assertNotNull(savedCard.getId());
        
    }

    @Test
    public void testDeleteCardById() {
        
        Card existingCard = cardService.getCards().get(0);
        int existingCardId = existingCard.getId();

        cardService.deleteCardById(existingCardId);

        assertNull(cardService.getById(existingCardId));
    }

    @Test
    public void testGetCardByName() {
        
        Card existingCard = cardService.getCards().get(0);
        String cardName = existingCard.getName();

        Card retrievedCard = cardService.getCardByName(cardName);

        assertNotNull(retrievedCard);
        assertEquals(cardName, retrievedCard.getName());
       
    }
}
