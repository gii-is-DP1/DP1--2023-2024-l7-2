package org.springframework.samples.dwarf.game;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.dwarf.game.Game;
import org.springframework.samples.dwarf.game.GameRepository;
import org.springframework.samples.dwarf.player.Player;

public class GameRepositoryTest {

    private GameRepository gameRepository;

    @BeforeEach
    public void setUp() {
        gameRepository = mock(GameRepository.class);
    }

    @Test
    public void testFindAll() {
        // Configurar el comportamiento del repositorio mock
        List<Game> mockGames = new ArrayList<>();
        when(gameRepository.findAll()).thenReturn(mockGames);

        // Ejecutar el método del repositorio y verificar el resultado
        List<Game> result = (List<Game>) gameRepository.findAll();
        assertEquals(mockGames, result, "La lista de juegos no coincide");
    }

    @Test
    public void testFindByNameContains() {
        // Configurar el comportamiento del repositorio mock
        String pattern = "Test";
        List<Game> mockGames = new ArrayList<>();
        when(gameRepository.findByNameContains(pattern)).thenReturn(mockGames);

        // Ejecutar el método del repositorio y verificar el resultado
        List<Game> result = (List<Game>) gameRepository.findByNameContains(pattern);
        assertEquals(mockGames, result, "La lista de juegos por nombre no coincide");
    }

    @Test
    public void testFindByStart() {
        // Configurar el comportamiento del repositorio mock
        LocalDateTime start = LocalDateTime.now();
        List<Game> mockGames = new ArrayList<>();
        when(gameRepository.findByStart(start)).thenReturn(mockGames);

        // Ejecutar el método del repositorio y verificar el resultado
        List<Game> result = (List<Game>) gameRepository.findByStart(start);
        assertEquals(mockGames, result, "La lista de juegos por fecha de inicio no coincide");
    }

    @Test
    public void testFindByFinish() {
        // Configurar el comportamiento del repositorio mock
        LocalDateTime finish = LocalDateTime.now().plusHours(1);
        List<Game> mockGames = new ArrayList<>();
        when(gameRepository.findByFinish(finish)).thenReturn(mockGames);

        // Ejecutar el método del repositorio y verificar el resultado
        List<Game> result = (List<Game>) gameRepository.findByFinish(finish);
        assertEquals(mockGames, result, "La lista de juegos por fecha de finalización no coincide");
    }

    @Test
    public void testFindByCode() {
        // Configurar el comportamiento del repositorio mock
        String code = "TEST123";
        List<Game> mockGames = new ArrayList<>();
        when(gameRepository.findByCode(code)).thenReturn(mockGames);

        // Ejecutar el método del repositorio y verificar el resultado
        List<Game> result = (List<Game>) gameRepository.findByCode(code);
        assertEquals(mockGames, result, "La lista de juegos por código no coincide");
    }

    @Test
    public void testFindByFinishIsNotNull() {
        // Configurar el comportamiento del repositorio mock
        List<Game> mockGames = new ArrayList<>();
        when(gameRepository.findByFinishIsNotNull()).thenReturn(mockGames);

        // Ejecutar el método del repositorio y verificar el resultado
        List<Game> result = (List<Game>) gameRepository.findByFinishIsNotNull();
        assertEquals(mockGames, result, "La lista de juegos por fecha de finalización no nula no coincide");
    }

    @Test
    public void testFindByFinishIsNullAndStartIsNotNull() {
        // Configurar el comportamiento del repositorio mock
        List<Game> mockGames = new ArrayList<>();
        when(gameRepository.findByFinishIsNullAndStartIsNotNull()).thenReturn(mockGames);

        // Ejecutar el método del repositorio y verificar el resultado
        List<Game> result = (List<Game>) gameRepository.findByFinishIsNullAndStartIsNotNull();
        assertEquals(mockGames, result,
                "La lista de juegos por fecha de finalización nula y fecha de inicio no nula no coincide");
    }
}
