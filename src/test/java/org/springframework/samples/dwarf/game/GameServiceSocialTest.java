package org.springframework.samples.dwarf.game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.CardService;
import org.springframework.samples.dwarf.dwarf.Dwarf;
import org.springframework.samples.dwarf.exceptions.CodeAlreadyTakenException;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.samples.dwarf.player.Player;
import org.springframework.samples.dwarf.user.User;
import org.springframework.samples.dwarf.user.UserService;
import org.springframework.test.context.junit4.SpringRunner;

import jakarta.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class GameServiceSocialTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private CardService cardService;

    @Autowired
    private UserService userService;

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

    @Test
    @Transactional
    void changePlayerStartTestChangesPlayer() {
        Card c = cardService.getById(32);

        Game g = gameService.getGameByCode("test-code");

        ArrayList<Pair<Player, Card>> helpCards = new ArrayList<>();   
        helpCards.add(Pair.of(g.getPlayerStart(),c));

        g = gameService.changePlayerStart(g, helpCards);

        assertEquals(g.getPlayers().get(2), g.getPlayerStart());
    }

    @Test
    @Transactional
    void changePlayerStartTestDoesNOTChangesPlayer() {
        Card c = cardService.getById(32);

        Game g = gameService.getGameByCode("test-code");

        Player p = g.getPlayers().get(0);

        assertNotEquals(p, g.getPlayerStart());

        ArrayList<Pair<Player, Card>> helpCards = new ArrayList<>();   
        helpCards.add(Pair.of(p,c));

        g = gameService.changePlayerStart(g, helpCards);

        assertEquals(g.getPlayers().get(1), g.getPlayerStart());
    }

    @Transactional
    @Test
    void checkIfIsPlayerTurnTestShouldReturnFalse() {
        Game g = gameService.getGameByCode("test-code");
        g.setRound(6);
        Player p = g.getPlayers().get(1);

        Boolean res = gameService.checkIfIsPlayerTurn(g, p);
        assertTrue(!res);
    }

    @Test
    @Transactional
    void checkIfIsPlayerTurnTestShouldReturnTrue() {
        Game g = gameService.getGameByCode("test-code");
        g.setRound(6);
        Player p = g.getPlayers().get(0);

        Boolean res = gameService.checkIfIsPlayerTurn(g, p);
        assertTrue(res);
    }

    @Test
    @Transactional
    void initalizeTest() {
        Game g = new Game();
        g.setCode("sample Code");

        User u = userService.findUserById(4);

        g = gameService.initalize(g, u);

        assertNotNull(g.getMainBoard());
        assertNotNull(g.getPlayerStart());
        assertNotNull(g.getPlayers());
        assertNotNull(g.getChat());
        assertEquals(1, g.getPlayers().size());
        assertEquals(g.getPlayers().get(0), g.getPlayerCreator());
        assertEquals(g.getPlayers().get(0), g.getPlayerStart());
    }

    @Test
    @Transactional
    void initalizeTestThrowErrorBecauseAlreadyUsedCode() {
        Game g = new Game();
        g.setCode("test-code");

        assertThrows(CodeAlreadyTakenException.class, () -> gameService.initalize(g, null));
    }
    
}
