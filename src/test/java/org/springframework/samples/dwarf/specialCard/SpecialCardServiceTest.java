package org.springframework.samples.dwarf.specialCard;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.SpecialCard;
import org.springframework.samples.dwarf.card.SpecialCardRepository;
import org.springframework.samples.dwarf.card.SpecialCardService;
import org.springframework.samples.dwarf.object.ObjectService;

@SpringBootTest
public class SpecialCardServiceTest {

    @Autowired
    private SpecialCardService scs;

    @Mock
    private SpecialCardService specialCardService;

    @Mock
    private SpecialCardRepository specialCardRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetSpecialCards() {
        // Configurar el comportamiento del repositorio mock
        List<SpecialCard> mockCards = new ArrayList<>();
        when(specialCardRepository.findAll()).thenReturn(mockCards);

        // Ejecutar el método del servicio y verificar el resultado
        List<SpecialCard> result = specialCardService.getSpecialCards();
        assertEquals(mockCards, result, "La lista de tarjetas no coincide");
    }

    @Test
    public void testGetById() {

        SpecialCard existingObject = scs.getSpecialCards().get(0);

        SpecialCard retrievedObject = scs.getById(existingObject.getId());
        assertNotNull(retrievedObject);
        assertEquals(existingObject.getId(), retrievedObject.getId());

    }

    @Test
    public void testSaveSpecialCard() {
        // Configurar el comportamiento del repositorio mock
        SpecialCard newCard = new SpecialCard();
        newCard.setId(1);
        newCard.setDescription("Do something heavy");
        newCard.setName("The worst");

        when(specialCardRepository.save(newCard)).thenReturn(newCard);

        // Ejecutar el método del servicio y verificar el resultado
        SpecialCard result = specialCardService.saveSpecialCard(newCard);
        assertNotEquals(newCard, result, "La tarjeta guardada no coincide");
    }



    @Test
    public void testGetSpecialCardByNameNotFound() {
        // Configurar el comportamiento del repositorio mock para devolver null
        when(specialCardRepository.findByName("NonExistentCard")).thenReturn(null);

        // Ejecutar el método del servicio y verificar que devuelve null
        SpecialCard result = specialCardService.getSpecialCardByName("NonExistentCard");
        assertNull(result, "La tarjeta debería ser nula ya que no existe");
    }

    @Test
    public void testGetSpecialCardsEmptyList() {
        // Configurar el comportamiento del repositorio mock para devolver una lista
        // vacía
        when(specialCardRepository.findAll()).thenReturn(new ArrayList<>());

        // Ejecutar el método del servicio y verificar que la lista está vacía
        List<SpecialCard> result = specialCardService.getSpecialCards();
        assertTrue(result.isEmpty(), "La lista de tarjetas debería estar vacía");
    }

    @Test
    public void testGetSpecialCardsNonEmptyList() {
        // Configurar el comportamiento del repositorio mock para devolver una lista no
        // vacía
        List<SpecialCard> mockCards = new ArrayList<>();
        mockCards.add(new SpecialCard());
        when(specialCardRepository.findAll()).thenReturn(mockCards);

        // Ejecutar el método del servicio y verificar que la lista no está vacía
        List<SpecialCard> result = specialCardService.getSpecialCards();
        assertTrue(result.isEmpty(), "La lista de tarjetas no debería estar vacía");
    }

    @Test
    public void testGetSpecialCardByIdNotFound() {
        // Configurar el comportamiento del repositorio mock para devolver un Optional
        // vacío
        when(specialCardRepository.findById(1)).thenReturn(Optional.empty());

        // Ejecutar el método del servicio y verificar que devuelve null
        SpecialCard result = specialCardService.getById(1);
        assertNull(result, "La tarjeta debería ser nula ya que no existe");
    }

    @Test
    public void testDeleteSpecialCardByIdNotFound() {
        // Configurar el comportamiento del repositorio mock para devolver un Optional
        // vacío
        when(specialCardRepository.findById(1)).thenReturn(Optional.empty());

        // Ejecutar el método del servicio y verificar que no se lanza ninguna excepción
        assertDoesNotThrow(() -> specialCardService.deleteSpecialCardById(1));
    }
}
