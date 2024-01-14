package org.springframework.samples.dwarf.cardDeck;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
public class cardDeckRepositoryTest {

    @Autowired
    private CardDeckRepository cardDeckRepository;

    @Test
    public void testFindAll() {

        List<CardDeck> cardDecks = cardDeckRepository.findAll();
        assertEquals(3, cardDecks.size());
    }

    @Test
    public void testGetCardDeckById_Positive() {
        CardDeck cardDeck = cardDeckRepository.getCardDeckById(1);

        assertNotNull(cardDeck);
        assertEquals(1, cardDeck.getId());
    }

    @Test
    public void testGetCardDeckById_Negative() {
        CardDeck cardDeck = cardDeckRepository.getCardDeckById(999);

        assertNull(cardDeck);
    }

}
