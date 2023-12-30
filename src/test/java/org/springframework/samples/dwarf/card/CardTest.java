package org.springframework.samples.dwarf.card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CardTest {

    @Test
    public void testCreateCard() {
        Card card = new Card();
        assertNotNull(card);
    }

    @Test
    public void testCardProperties() {
        Card card = new Card();
        card.setName("Test Card");
        card.setDescription("Description");
        card.setPosition(1);

        CardType cardType = new CardType();
        card.setCardType(cardType);

        card.setTotalIron(10);
        card.setTotalGold(20);
        card.setTotalSteal(5);
        card.setTotalMedals(3);

        assertEquals("Test Card", card.getName());
        assertEquals("Description", card.getDescription());
        assertEquals(1, card.getPosition());
        assertEquals(cardType, card.getCardType());
        assertEquals(10, card.getTotalIron());
        assertEquals(20, card.getTotalGold());
        assertEquals(5, card.getTotalSteal());
        assertEquals(3, card.getTotalMedals());
    }

    @Test
    public void testCardTypeAssociation() {
        Card card = new Card();
        assertNull(card.getCardType());

        CardType cardType = new CardType();

        card.setCardType(cardType);

        assertNotNull(card.getCardType());
        assertEquals(cardType, card.getCardType());
    }
}
