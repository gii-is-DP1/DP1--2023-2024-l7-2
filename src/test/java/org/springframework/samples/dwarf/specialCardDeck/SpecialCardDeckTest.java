package org.springframework.samples.dwarf.specialCardDeck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.dwarf.card.SpecialCard;
import org.springframework.samples.dwarf.specialCardDeck.SpecialCardDeck;

@SpringBootTest
public class SpecialCardDeckTest {

    @Test
    public void testCreateSpecialCardDeck() {
        SpecialCardDeck specialCardDeck = new SpecialCardDeck();
        assertNotNull(specialCardDeck);
    }

    @Test
    public void testSpecialCardDeckProperties() {
        SpecialCardDeck specialCardDeck = new SpecialCardDeck();

        // Configura las propiedades según tus necesidades
        SpecialCard specialCard1 = new SpecialCard();
        SpecialCard specialCard2 = new SpecialCard();

        List<SpecialCard> specialCards = new ArrayList<>();
        specialCards.add(specialCard1);
        specialCards.add(specialCard2);

        specialCardDeck.setSpecialCards(specialCards);

        SpecialCard lastSpecialCard = new SpecialCard();
        specialCardDeck.setLastSpecialCard(lastSpecialCard);

        // Asegúrate de que las propiedades se hayan establecido correctamente
        assertEquals(specialCards, specialCardDeck.getSpecialCards());
        assertEquals(lastSpecialCard, specialCardDeck.getLastSpecialCard());
    }

    @Test
    public void testSpecialCardAssociation() {
        SpecialCardDeck specialCardDeck = new SpecialCardDeck();
        assertNull(specialCardDeck.getLastSpecialCard());

        SpecialCard specialCard = new SpecialCard();
        // Configura el objeto SpecialCard según tus necesidades

        specialCardDeck.setLastSpecialCard(specialCard);

        assertNotNull(specialCardDeck.getLastSpecialCard());
        assertEquals(specialCard, specialCardDeck.getLastSpecialCard());
    }
}