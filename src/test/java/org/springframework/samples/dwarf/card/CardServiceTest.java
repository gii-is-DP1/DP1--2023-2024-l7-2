package org.springframework.samples.dwarf.card;

import static org.junit.Assert.assertTrue;
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

        Integer id = 1;
        String name = "Iron Seam";
        Integer position = 1;

        Card retrievedCard = cardService.getById(id);
        assertNotNull(retrievedCard);
        assertEquals(id, retrievedCard.getId());
        assertEquals(name, retrievedCard.getName());
        assertEquals(position, retrievedCard.getPosition());

    }

    @Test
    public void testSaveCard() {
        Card newCard = new Card();

        CardType newCardType = cardService.getById(1).getCardType();

        newCard.setName("sample name");
        newCard.setDescription("sample description");
        newCard.setPosition(2);
        newCard.setCardType(newCardType);

        Card savedCard = cardService.saveCard(newCard);

        assertNotNull(savedCard.getId());

        assertEquals(savedCard.getId(),100);

    }

    @Test
    public void testDeleteCardById() {

        int existingCardId = 1;
        Card existingCard = cardService.getById(existingCardId);

        assertNotNull(existingCard);

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
