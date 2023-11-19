package org.springframework.samples.dwarf.specialCard;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.dwarf.card.SpecialCard;
import org.springframework.samples.dwarf.card.SpecialCardRepository;
import org.springframework.samples.dwarf.card.SpecialCardService;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
public class SpecialCardServiceTest {

    @Autowired
    private SpecialCardService specialCardService;

    @MockBean
    private SpecialCardRepository specialCardRepository;

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
        // Configurar el comportamiento del repositorio mock
        SpecialCard mockCard = new SpecialCard();
        mockCard.setId(1);
        when(specialCardRepository.findById(1)).thenReturn(Optional.of(mockCard));

        // Ejecutar el método del servicio y verificar el resultado
        SpecialCard result = specialCardService.getById(1);
        assertEquals(mockCard, result, "La tarjeta recuperada no coincide");
    }

    @Test
    public void testSaveSpecialCard() {
        // Configurar el comportamiento del repositorio mock
        SpecialCard newCard = new SpecialCard();
        newCard.setId(1);
        when(specialCardRepository.save(newCard)).thenReturn(newCard);

        // Ejecutar el método del servicio y verificar el resultado
        SpecialCard result = specialCardService.saveSpecialCard(newCard);
        assertEquals(newCard, result, "La tarjeta guardada no coincide");
    }

    @Test
    public void testDeleteSpecialCardById() {
        // Ejecutar el método del servicio
        specialCardService.deleteSpecialCardById(1);

        // Verificar que el método deleteById del repositorio mock se llamó con el ID
        // correcto
        verify(specialCardRepository, times(1)).deleteById(1);
    }

    @Test
    public void testGetSpecialCardByName() {
        // Configurar el comportamiento del repositorio mock
        SpecialCard mockCard = new SpecialCard();
        mockCard.setId(1);
        when(specialCardRepository.findByName("TestCard")).thenReturn(mockCard);

        // Ejecutar el método del servicio y verificar el resultado
        SpecialCard result = specialCardService.getSpecialCardByName("TestCard");
        assertEquals(mockCard, result, "La tarjeta recuperada por nombre no coincide");
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
        // Configurar el comportamiento del repositorio mock para devolver una lista vacía
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
        assertFalse(result.isEmpty(), "La lista de tarjetas no debería estar vacía");
    }

    @Test
    public void testGetSpecialCardByIdNotFound() {
        // Configurar el comportamiento del repositorio mock para devolver un Optional vacío
        when(specialCardRepository.findById(1)).thenReturn(Optional.empty());

        // Ejecutar el método del servicio y verificar que devuelve null
        SpecialCard result = specialCardService.getById(1);
        assertNull(result, "La tarjeta debería ser nula ya que no existe");
    }

    @Test
    public void testDeleteSpecialCardByIdNotFound() {
        // Configurar el comportamiento del repositorio mock para devolver un Optional vacío
        when(specialCardRepository.findById(1)).thenReturn(Optional.empty());

        // Ejecutar el método del servicio y verificar que no se lanza ninguna excepción
        assertDoesNotThrow(() -> specialCardService.deleteSpecialCardById(1));
    }
}
