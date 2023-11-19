package org.springframework.samples.dwarf.specialCard;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.dwarf.card.SpecialCard;
import org.springframework.samples.dwarf.card.SpecialCardRestController;
import org.springframework.samples.dwarf.card.SpecialCardService;
import org.springframework.validation.BindingResult;

@SpringBootTest
public class SpecialCardRestControllerTest {

    @Autowired
    private SpecialCardRestController specialCardRestController;

    @MockBean
    private SpecialCardService specialCardService;

    @Test
    public void testFindAll() {
        // Configurar el comportamiento del servicio mock
        List<SpecialCard> mockCards = new ArrayList<>();
        when(specialCardService.getSpecialCards()).thenReturn(mockCards);

        // Ejecutar el método del controlador y verificar el resultado
        ResponseEntity<List<SpecialCard>> result = specialCardRestController.findAll();
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Código de estado incorrecto");
        assertEquals(mockCards, result.getBody(), "La lista de tarjetas no coincide");
    }

    @Test
    public void testFindSpecialCard() {
        // Configurar el comportamiento del servicio mock
        SpecialCard mockCard = new SpecialCard();
        mockCard.setId(1);
        when(specialCardService.getById(1)).thenReturn(mockCard);

        // Ejecutar el método del controlador y verificar el resultado
        ResponseEntity<SpecialCard> result = specialCardRestController.findSpecialCard(1);
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Código de estado incorrecto");
        assertEquals(mockCard, result.getBody(), "La tarjeta recuperada no coincide");
    }

    @Test
    public void testCreateSpecialCard() {
        // Configurar el comportamiento del servicio mock
        SpecialCard newCard = new SpecialCard();
        when(specialCardService.saveSpecialCard(newCard)).thenReturn(newCard);

        // Ejecutar el método del controlador y verificar el resultado
        ResponseEntity<SpecialCard> result = specialCardRestController.createSpecialCard(newCard,
                mock(BindingResult.class));
        assertEquals(HttpStatus.CREATED, result.getStatusCode(), "Código de estado incorrecto");
        assertEquals(newCard, result.getBody(), "La tarjeta creada no coincide");
    }

    @Test
    public void testModifySpecialCard() {
        // Configurar el comportamiento del servicio mock
        SpecialCard existingCard = new SpecialCard();
        existingCard.setId(1);
        when(specialCardService.getById(1)).thenReturn(existingCard);

        SpecialCard modifiedCard = new SpecialCard();
        modifiedCard.setId(1);
        modifiedCard.setName("ModifiedCard");

        when(specialCardService.saveSpecialCard(existingCard)).thenReturn(existingCard);

        // Ejecutar el método del controlador y verificar el resultado
        ResponseEntity<Void> result = specialCardRestController.modifySpecialCard(modifiedCard,
                mock(BindingResult.class), 1);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode(), "Código de estado incorrecto");
    }

    @Test
    public void testDeleteSpecialCard() {
        // Configurar el comportamiento del servicio mock
        SpecialCard existingCard = new SpecialCard();
        existingCard.setId(1);
        when(specialCardService.getById(1)).thenReturn(existingCard);

        // Ejecutar el método del controlador y verificar el resultado
        ResponseEntity<Void> result = specialCardRestController.deleteSpecialCard(1);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode(), "Código de estado incorrecto");
    }
}
