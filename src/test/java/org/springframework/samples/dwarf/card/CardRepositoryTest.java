package org.springframework.samples.dwarf.card;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.CardRepository;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
public class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;

    @Test
    public void testFindAll() {
        List<Card> cards = cardRepository.findAll();
        assertEquals(54, cards.size());
    }

    @Test
    public void testFindByName() {
        Card existingCard = cardRepository.findAll().get(0);
        String cardName = existingCard.getName();

        List<Card> cards = cardRepository.findByName(cardName);

        assertEquals(10, cards.size()); // 10 cartas de Iron Seam
        assertEquals(cardName, cards.get(0).getName());

    }

}
