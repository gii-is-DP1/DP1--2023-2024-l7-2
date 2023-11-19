package org.springframework.samples.dwarf.specialCard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.samples.dwarf.card.SpecialCard;
import org.springframework.samples.dwarf.card.SpecialCardRepository;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class SpecialCardRepositoryTest {

    @Autowired
    private SpecialCardRepository specialCardRepository;

    @Test
    @Transactional
    @DirtiesContext // Necesario para limpiar el contexto después de la prueba
    public void testSaveAndFindAll() {
        // Guardar una tarjeta especial
        SpecialCard specialCard = new SpecialCard();
        specialCard.setName("TestCard");
        specialCard.setDescription("TestDescription");

        SpecialCard savedCard = specialCardRepository.save(specialCard);

        assertNotNull(savedCard.getId(), "ID no debería ser nulo después de guardar");

        // Buscar todas las tarjetas especiales
        List<SpecialCard> allCards = specialCardRepository.findAll();

        assertEquals(10, allCards.size(), "Debería haber diez tarjetas especiales en la base de datos");
        assertEquals("Muster an army", allCards.get(0).getName(), "Nombre de la tarjeta especial incorrecto");
        assertEquals("When resolving actions this round, treat all defend locations as if they are occupied.",
                allCards.get(0).getDescription(), "Descripción de la tarjeta especial incorrecta");
    }

    @Test
    @Transactional
    @DirtiesContext
    public void testFindByName() {
        // Guardar una tarjeta especial
        SpecialCard specialCard = new SpecialCard();
        specialCard.setName("TestCard");
        specialCard.setDescription("TestDescription");
        specialCardRepository.save(specialCard);

        // Buscar la tarjeta especial por nombre
        SpecialCard foundCard = specialCardRepository.findByName("TestCard");

        assertNotNull(foundCard, "Debería haber encontrado una tarjeta especial");
        assertEquals("TestCard", foundCard.getName(), "Nombre de la tarjeta especial incorrecto");
        assertEquals("TestDescription", foundCard.getDescription(), "Descripción de la tarjeta especial incorrecta");
    }

    @Test
    @Transactional
    @DirtiesContext
    public void testFindByNameNotFound() {
        // Intentar buscar una tarjeta especial por un nombre que no existe
        SpecialCard foundCard = specialCardRepository.findByName("NonExistentCard");

        assertNull(foundCard, "No debería haber encontrado una tarjeta especial con este nombre");
    }

}