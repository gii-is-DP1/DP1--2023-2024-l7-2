package org.springframework.samples.dwarf.game;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.dwarf.card.SpecialCard;
import org.springframework.samples.dwarf.object.Object;
import org.springframework.samples.dwarf.player.Player;
import org.springframework.samples.dwarf.player.PlayereService;
import org.springframework.samples.dwarf.user.User;
import org.springframework.samples.dwarf.user.UserService;

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

        gameRestController = new GameRestController(gameService, userService, playerService, null, null, null, null);
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
/*
    @Test
    public void testSpecialOrder() {
        // Configurar objetos de prueba
        Game game = new Game();
        // Configurar otros objetos según sea necesario

        // Simular comportamiento de servicios
        when(gameService.getGameByCode(anyString())).thenReturn(game);
        when(gameService.checkPlayerInGameAndGameExists(any(Game.class))).thenReturn(true);

        // Configurar el usuario actual
        when(userService.findCurrentUser()).thenReturn(new User());

        // Configurar el jugador actual
        Player currentPlayer = new Player();
        List<Object> s = new ArrayList<Object>();
        currentPlayer.setGold(10);
        currentPlayer.setIron(10);
        currentPlayer.setSteal(10);
        currentPlayer.setObjects(s);
        // Configurar el estado del jugador según sea necesario
        when(playerService.getPlayerByUserAndGame(any(User.class), any(Game.class))).thenReturn(currentPlayer);

        // Llamar al método del controlador
        SpecialCard sp = new SpecialCard();
        Integer x = 1;
        Integer y = 2;
        Integer z= 2;
        Object o = new Object();
        o.setName("Axe");
        sp.setName("Special Order");
        
        ResponseEntity<Void> response = gameRestController
        .handleSpecialAction2(
                sp,
                "testCode",
                x, y, z, o);
                // Verificar el resultado
                assertEquals(HttpStatus.OK, response.getStatusCode());
                // Verificar otros aspectos según sea necesario
            }
            
            */
}

    

