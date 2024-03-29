package org.springframework.samples.dwarf.game;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.dwarf.dwarf.Dwarf;
import org.springframework.samples.dwarf.game.Game;
import org.springframework.samples.dwarf.mainboard.MainBoard;
import org.springframework.samples.dwarf.player.Player;
import org.springframework.samples.dwarf.user.User;

public class GameTest {

    private Game game;

    @BeforeEach
    public void setUp() {
        game = new Game();
    }

    @Test
    public void testSetName() {
        String name = "Test Game";
        game.setName(name);
        assertEquals(name, game.getName(), "El nombre del juego no coincide");
    }

    @Test
    public void testSetCode() {
        String code = "TEST123";
        game.setCode(code);
        assertEquals(code, game.getCode(), "El código del juego no coincide");
    }

    @Test
    public void testSetStart() {
        LocalDateTime start = LocalDateTime.now();
        game.setStart(start);
        assertEquals(start, game.getStart(), "La fecha de inicio del juego no coincide");
    }

    @Test
    public void testSetFinish() {
        LocalDateTime finish = LocalDateTime.now().plusHours(1);
        game.setFinish(finish);
        assertEquals(finish, game.getFinish(), "La fecha de finalización del juego no coincide");
    }

    @Test
    public void testSetWinnerId() {
        User winner = new User();
        game.setUserWinner(winner);
        assertEquals(winner, game.getUserWinner(), "El ganador del juego no coincide");
    }

    @Test
    public void testSetRound() {
        Integer round = 2;
        game.setRound(round);
        assertEquals(round, game.getRound(), "La ronda del juego no coincide");
    }

    @Test
    public void testSetMainBoard() {
        MainBoard mainBoard = new MainBoard();

        game.setMainBoard(mainBoard);
        assertEquals(mainBoard, game.getMainBoard(), "El tablero principal del juego no coincide");
    }

    @Test
    public void testSetDwarves() {
        List<Dwarf> dwarves = new ArrayList<>();
        Dwarf dwarf1 = new Dwarf();
        dwarves.add(dwarf1);

        Dwarf dwarf2 = new Dwarf();
        dwarves.add(dwarf2);

        game.setDwarves(dwarves);
        assertEquals(dwarves, game.getDwarves(), "La lista de enanos del juego no coincide");
    }

    @Test
    public void testSetPlayerCreator() {
        Player playerCreator = new Player();
        playerCreator.setName("PlayerCreator");
        game.setPlayerCreator(playerCreator);
        assertEquals(playerCreator, game.getPlayerCreator(), "El jugador creador del juego no coincide");
    }
}
