package org.springframework.samples.petclinic.game;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.dwarf.Dwarf;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.game.GameRepository;
import org.springframework.samples.petclinic.game.GameService;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerRepository;

public class GameServiceTest {

    private GameService gameService;
    private GameRepository gameRepository;
    private PlayerRepository playerRepository;

    @BeforeEach
    public void setUp() {
        gameRepository = mock(GameRepository.class);
        playerRepository = mock(PlayerRepository.class);
        gameService = new GameService(gameRepository, playerRepository);
    }

    @Test
    public void testGetAllGames() {
        List<Game> mockGames = Arrays.asList(new Game(), new Game());
        when(gameRepository.findAll()).thenReturn(mockGames);

        List<Game> result = gameService.getAllGames();
        assertEquals(2, result.size(), "Número incorrecto de juegos recuperados");
    }

    @Test
    public void testDelete() {
        Game entityToDelete = new Game();
        when(gameService.getGameById(1)).thenReturn(Optional.of(entityToDelete));

        gameService.delete(1);
        verify(gameRepository).deleteById(1);
    }

    @Test
    public void testGetGameByCode() {
        String gameCode = "TEST123";
        Game mockGame = new Game();
        when(gameRepository.findByCode(gameCode)).thenReturn(Collections.singletonList(mockGame));

        Game result = gameService.getGameByCode(gameCode);
        assertNotNull(result, "Juego no encontrado por código");
    }

    @Test
    public void testGetFinishedGames() {
        List<Game> mockFinishedGames = Arrays.asList(new Game(), new Game());
        when(gameRepository.findByFinishIsNotNull()).thenReturn(mockFinishedGames);
        List<Game> result = gameService.getFinishedGames();
        assertEquals(2, result.size(), "Número incorrecto de juegos finalizados recuperados");
    }
}
