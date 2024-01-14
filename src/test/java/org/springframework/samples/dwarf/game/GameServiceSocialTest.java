package org.springframework.samples.dwarf.game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.dwarf.dwarf.Dwarf;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.dwarf.player.Player;
import org.springframework.test.context.junit4.SpringRunner;

import jakarta.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class GameServiceSocialTest {

    @Autowired
    private GameService gameService;


    @Test
    @Transactional
    void testTurnWhenPlayerStarterIsFirstPlayer(){

        Game g = gameService.getGameByCode("test-code");
        
        List<Player> players = g.getPlayers();
        Player owner1 = players.get(0);
        Player owner2 = players.get(1);
        Player owner3 = players.get(2);
        
        Player starter = owner1;

        List<Dwarf> dwarves = g.getDwarves();
        Integer round = g.getRound();
        List<Dwarf> thisRoundDwarves = dwarves.stream().filter(d -> d.getRound() == round
                && d.getPlayer() != null).toList();

        List<Player> returnedPlayers = gameService.getRemainingTurns(players,thisRoundDwarves,starter);

        List<Player> expected = List.of(owner1,owner2,owner3,owner1,owner2,owner3);

        assertEquals(returnedPlayers.get(0).getName(), starter.getName());
        assertEquals(returnedPlayers, expected);

    }

    @Test
    @Transactional
    void testTurnWhenPlayerStarterIsSecondPlayer(){

        Game g = gameService.getGameByCode("test-code");
        
        List<Player> players = g.getPlayers();
        Player owner1 = players.get(0);
        Player owner2 = players.get(1);
        Player owner3 = players.get(2);
        
        Player starter = owner2;

        List<Dwarf> dwarves = g.getDwarves();
        Integer round = g.getRound();
        List<Dwarf> thisRoundDwarves = dwarves.stream().filter(d -> d.getRound() == round
                && d.getPlayer() != null).toList();

        List<Player> returnedPlayers = gameService.getRemainingTurns(players,thisRoundDwarves,starter);

        List<Player> expected = List.of(owner2,owner3,owner1,owner2,owner3,owner1);

        assertEquals(returnedPlayers.get(0).getName(), starter.getName());
        assertEquals(returnedPlayers, expected);

    }

    @Test
    @Transactional
    void testTurnWhenPlayerStarterIsThirdPlayer(){

        Game g = gameService.getGameByCode("test-code");
        
        List<Player> players = g.getPlayers();
        Player owner1 = players.get(0);
        Player owner2 = players.get(1);
        Player owner3 = players.get(2);
        
        Player starter = owner3;

        List<Dwarf> dwarves = g.getDwarves();
        Integer round = g.getRound();
        List<Dwarf> thisRoundDwarves = dwarves.stream().filter(d -> d.getRound() == round
                && d.getPlayer() != null).toList();

        List<Player> returnedPlayers = gameService.getRemainingTurns(players,thisRoundDwarves,starter);

        List<Player> expected = List.of(owner3,owner1,owner2,owner3,owner1,owner2);

        assertEquals(returnedPlayers.get(0).getName(), starter.getName());
        assertEquals(returnedPlayers, expected);

    }

    @Test
    @Transactional
    void testTurnWhenHelpCardUsed(){

        Game g = gameService.getGameByCode("test-code");
        
        List<Player> players = g.getPlayers();
        Player owner1 = players.get(0);
        Player owner2 = players.get(1);
        Player owner3 = players.get(2);
        
        Player starter = owner3;

        List<Dwarf> dwarves = g.getDwarves();
        Integer round = 2;
        List<Dwarf> thisRoundDwarves = dwarves.stream().filter(d -> d.getRound() == round
                && d.getPlayer() != null).toList();

        List<Player> returnedPlayers = gameService.getRemainingTurns(players,thisRoundDwarves,starter);

        List<Player> expected = List.of(owner3,owner1,owner2,owner3,owner1,owner2, 
            owner2,owner2);
        // Los dos turnos de mas son por la carta de ayuda, es lo que se esta comprobando

        assertEquals(returnedPlayers.get(0).getName(), starter.getName());
        assertEquals(returnedPlayers, expected);

    }

    @Test
    @Transactional
    void testTurnWhenTwoSpecialCardUsedWithOneDwarf(){

        Game g = gameService.getGameByCode("test-code");
        
        List<Player> players = g.getPlayers();
        Player owner1 = players.get(0);
        Player owner2 = players.get(1);
        Player owner3 = players.get(2);
        
        Player starter = owner1;

        List<Dwarf> dwarves = g.getDwarves();
        Integer round = 3;
        List<Dwarf> thisRoundDwarves = dwarves.stream().filter(d -> d.getRound() == round
                && d.getPlayer() != null).toList();

        List<Player> returnedPlayers = gameService.getRemainingTurns(players,thisRoundDwarves,starter);

        List<Player> expected = List.of(owner1,owner1,owner3,owner2,owner3,owner2);
        // Tanto el owner3 como el owner2 han tirado una carta especial con un solo dwarf
        // Por lo tanto tienen que tirar su turno al final de la ronda en sentido contrario

        assertEquals(returnedPlayers.get(0).getName(), starter.getName());
        assertEquals(returnedPlayers, expected);
    }

    @Test
    @Transactional
    void testTurnWhenOneSpecialCardUsedWithOneDwarf(){

        Game g = gameService.getGameByCode("test-code");
        
        List<Player> players = g.getPlayers();
        Player owner1 = players.get(0);
        Player owner2 = players.get(1);
        Player owner3 = players.get(2);
        
        Player starter = owner1;

        List<Dwarf> dwarves = g.getDwarves();
        Integer round = 5;
        List<Dwarf> thisRoundDwarves = dwarves.stream().filter(d -> d.getRound() == round
                && d.getPlayer() != null).toList();

        List<Player> returnedPlayers = gameService.getRemainingTurns(players,thisRoundDwarves,starter);

        List<Player> expected = List.of(owner2,owner3,owner2,owner3,owner1,owner1);

        System.out.println(returnedPlayers);

        assertEquals(returnedPlayers, expected);
    }

    @Test
    @Transactional
    void testTurnWhenSpecialCardGivesExtraDwarf(){

        Game g = gameService.getGameByCode("test-code");
        
        List<Player> players = g.getPlayers();
        Player owner1 = players.get(0);
        Player owner2 = players.get(1);
        Player owner3 = players.get(2);
        
        Player starter = owner1;

        List<Dwarf> dwarves = g.getDwarves();
        Integer round = 4;
        List<Dwarf> thisRoundDwarves = dwarves.stream().filter(d -> d.getRound() == round
                && d.getPlayer() != null).toList();

        List<Player> returnedPlayers = gameService.getRemainingTurns(players,thisRoundDwarves,starter);

        System.out.println(returnedPlayers);

        List<Player> expected = List.of(owner1,owner2,owner3,owner1,owner2,owner3,owner2);

        assertEquals(returnedPlayers.get(0).getName(), starter.getName());
        assertEquals(returnedPlayers, expected);
    }

    @Test
    @Transactional
    void testRoundNeedsChangeShouldReturnTrue(){

        Game g = gameService.getGameByCode("test-code");

        List<Dwarf> dwarves = g.getDwarves();
        Integer round = 4;
        List<Dwarf> thisRoundDwarves = dwarves.stream().filter(d -> d.getRound() == round
                && d.getPlayer() != null).toList();

        g.setRound(round);
        Boolean res = gameService.checkRoundNeedsChange(g, thisRoundDwarves);
        Boolean expected = true;

        assertEquals(res, expected);
    }

    @Test
    @Transactional
    void testRoundNeedsChangeShouldReturnFalse(){

        Game g = gameService.getGameByCode("test-code");

        List<Dwarf> dwarves = g.getDwarves();
        Integer round = 1;
        List<Dwarf> thisRoundDwarves = dwarves.stream().filter(d -> d.getRound() == round
                && d.getPlayer() != null).toList();

        g.setRound(round);
        Boolean res = gameService.checkRoundNeedsChange(g, thisRoundDwarves);
        Boolean expected = false;

        assertEquals(res, expected);
    }
}
