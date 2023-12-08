package org.springframework.samples.dwarf.cardDeck;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class cardDeckRepositoryTest {

    @Autowired
    private CardDeckRepository cardDeckRepository;

    @Test
    public void testFindAll() {
        List<CardDeck> cardDecks = cardDeckRepository.findAll();
        assertEquals(1, cardDecks.size());
    }

    @Test
    public void testGetCardDeckById() {
        Integer cardDeckId = 1;
        CardDeck cardDeck = cardDeckRepository.getCardDeckById(cardDeckId);
        assertEquals(cardDeckId, cardDeck.getId());
    }

}
