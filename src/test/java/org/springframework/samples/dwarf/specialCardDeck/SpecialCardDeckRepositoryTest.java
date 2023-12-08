package org.springframework.samples.dwarf.specialCardDeck;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class SpecialCardDeckRepositoryTest {

    @Autowired
    private SpecialCardDeckRepository specialCardDeckRepository;

    @Mock
    private SpecialCardDeckService specialCardDeckService;

    @Test
    public void testFindAll() {
        List<SpecialCardDeck> specialCardDecks = specialCardDeckRepository.findAll();
        assertEquals(3, specialCardDecks.size());
    }

    @Test
    public void testFindById() {
        Integer specialCardDeckId = 1;
        Optional<SpecialCardDeck> optionalSpecialCardDeck = specialCardDeckRepository.findById(specialCardDeckId);
        assertEquals(specialCardDeckId, optionalSpecialCardDeck.get().getId());
    }

    @Test
    public void testFindById_empty() {
        Integer specialCardDeckId = 4;
        Optional<SpecialCardDeck> optionalSpecialCardDeck = specialCardDeckRepository.findById(specialCardDeckId);

        assertEquals(Optional.empty(), optionalSpecialCardDeck); // Cambia el valor esperado según la lógica de tu
                                                                 // aplicación
    }

}
