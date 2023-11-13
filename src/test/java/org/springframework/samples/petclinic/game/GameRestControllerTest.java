package org.springframework.samples.petclinic.game;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.dwarf.Dwarf;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.game.GameRestController;
import org.springframework.samples.petclinic.game.GameService;
import org.springframework.samples.petclinic.player.PlayereService;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;

public class GameRestControllerTest {

    private GameRestController gameRestController;

    private GameService gameService;
    private UserService userService;
    private PlayereService playerService;

    @BeforeEach
    public void setUp() {
        gameService = mock(GameService.class);
        userService = mock(UserService.class);
        playerService = mock(PlayereService.class);

        gameRestController = new GameRestController(gameService, userService, playerService, null, null, null);
    }

    @Test
    public void testGetAllGames() {
        // Configurar el comportamiento del servicio mock
        List<Game> mockGames = new ArrayList<>();
        when(gameService.getAllGames()).thenReturn(mockGames);

        // Ejecutar el método del controlador y verificar el resultado
        List<Game> result = gameRestController.getAllGames(null, null);
        assertEquals(mockGames, result, "La lista de juegos no coincide");
    }

    @Test
    public void testGetGameById() {
        // Configurar el comportamiento del servicio mock
        Integer gameId = 1;
        Game mockGame = new Game();
        when(gameService.getGameById(gameId)).thenReturn(Optional.of(mockGame));

        // Ejecutar el método del controlador y verificar el resultado
        Game result = gameRestController.getGameById(gameId);
        assertEquals(mockGame, result, "El juego recuperado no coincide");
    }

    @Test
    public void testGetGameByCode() {
        // Configurar el comportamiento del servicio mock
        String gameCode = "TEST123";
        Game mockGame = new Game();
        when(gameService.getGameByCode(gameCode)).thenReturn(mockGame);

        // Ejecutar el método del controlador y verificar el resultado
        ResponseEntity<Void> result = gameRestController.getGameByCode(gameCode);
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Código de estado incorrecto");
    }

    @Test
    public void testPlayGame() {
        // Configurar el comportamiento del servicio mock
        String gameCode = "TEST123";
        Game mockGame = new Game();
        when(gameService.getGameByCode(gameCode)).thenReturn(mockGame);

        // Ejecutar el método del controlador y verificar el resultado
        ResponseEntity<Game> result = gameRestController.playGame(gameCode);
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Código de estado incorrecto");
        assertEquals(mockGame, result.getBody(), "El juego recuperado no coincide");
    }

     @Test
    public void testJoinGame() {
        // Configurar el comportamiento del servicio mock
        String gameCode = "TEST123";
        Game mockGame = new Game();
        when(gameService.getGameByCode(gameCode)).thenReturn(mockGame);

        User mockUser = new User();
        when(userService.findCurrentUser()).thenReturn(mockUser);

        // Ejecutar el método del controlador y verificar el resultado
        ResponseEntity<Game> result = gameRestController.joinGame(gameCode);
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Código de estado incorrecto");

        // Verificar que se haya creado un nuevo jugador correctamente
        verify(playerService, times(1)).savePlayer(any(Player.class));
    }




}