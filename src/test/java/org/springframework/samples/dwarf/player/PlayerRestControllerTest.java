package org.springframework.samples.dwarf.player;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.dwarf.exceptions.BadRequestException;
import org.springframework.samples.dwarf.exceptions.ResourceNotFoundException;
import org.springframework.samples.dwarf.player.Player;
import org.springframework.samples.dwarf.player.PlayerRestController;
import org.springframework.samples.dwarf.player.PlayerService;
import org.springframework.validation.BindingResult;

@SpringBootTest
public class PlayerRestControllerTest {

    @Autowired
    private PlayerRestController playerRestController;

    @MockBean
    private PlayerService playerService;

    @Test
    public void testFindAll() {
        // Configurar el comportamiento del servicio mock
        List<Player> mockPlayers = new ArrayList<>();
        when(playerService.getPlayers()).thenReturn(mockPlayers);

        // Ejecutar el método del controlador y verificar el resultado
        ResponseEntity<List<Player>> result = playerRestController.findAll();
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Código de estado incorrecto");
        assertEquals(mockPlayers, result.getBody(), "La lista de jugadores no coincide");
    }

    @Test
    public void testCreatePlayer() {
        // Configurar el comportamiento del servicio mock
        Player newPlayer = new Player();
        when(playerService.savePlayer(newPlayer)).thenReturn(newPlayer);

        // Ejecutar el método del controlador y verificar el resultado
        ResponseEntity<Player> result = playerRestController.createPlayer(newPlayer, mock(BindingResult.class));
        assertEquals(HttpStatus.CREATED, result.getStatusCode(), "Código de estado incorrecto");
        assertEquals(newPlayer, result.getBody(), "El jugador creado no coincide");
    }

    @Test
    public void testCreatePlayerWithValidationError() {
        // Configurar el comportamiento del servicio mock para simular errores de
        // validación
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // Ejecutar el método del controlador y verificar que se lance una
        // BadRequestException
        assertThrows(BadRequestException.class, () -> playerRestController.createPlayer(new Player(), bindingResult));
    }

    @Test
    public void testGetPlayers() {
        int playerId = 1;
        Player mockPlayer = new Player();
        mockPlayer.setId(playerId);
        mockPlayer.setName("TestPlayer");

        when(playerService.getById(playerId)).thenReturn(mockPlayer);

        ResponseEntity<Player> responseEntity = playerRestController.getPlayers(playerId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockPlayer, responseEntity.getBody());

        verify(playerService, times(1)).getById(playerId);
    }

    @Test
    public void testGetPlayers_PlayerNotFound() {
        int playerId = 2;
        when(playerService.getById(playerId)).thenReturn(null);
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> playerRestController.getPlayers(playerId));
        assertEquals("Player with id " + playerId + " not found!", exception.getMessage());
        verify(playerService, times(1)).getById(playerId);
    }
}