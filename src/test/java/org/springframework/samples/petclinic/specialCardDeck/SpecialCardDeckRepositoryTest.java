package org.springframework.samples.petclinic.specialCardDeck;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class SpecialCardDeckRepositoryTest {

    @Autowired
    private SpecialCardDeckRepository specialCardDeckRepository;

    @Test
    public void testFindAll() {
        List<SpecialCardDeck> specialCardDecks = new ArrayList<>();
        specialCardDeckRepository.findAll().forEach(specialCardDecks::add);

        assertEquals(0, specialCardDecks.size()); // Cambia el valor esperado según la lógica de tu aplicación
    }

    @Test
    public void testFindById() {
        // Asegúrate de tener lógica para guardar un SpecialCardDeck y obtener su ID
        // para realizar esta prueba
        int specialCardDeckId = 1;
        Optional<SpecialCardDeck> optionalSpecialCardDeck = specialCardDeckRepository.findById(specialCardDeckId);

        assertEquals(Optional.empty(), optionalSpecialCardDeck); // Cambia el valor esperado según la lógica de tu aplicación
    }

    // Agrega más pruebas según sea necesario para los otros métodos del repositorio
}
