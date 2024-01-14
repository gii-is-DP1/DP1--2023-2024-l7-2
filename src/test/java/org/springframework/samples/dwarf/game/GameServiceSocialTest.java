package org.springframework.samples.dwarf.game;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.runner.RunWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.CardType;
import org.springframework.samples.dwarf.cardDeck.CardDeckService;
import org.springframework.samples.dwarf.dwarf.Dwarf;
import org.springframework.samples.dwarf.game.Game;
import org.springframework.samples.dwarf.game.GameRepository;
import org.springframework.samples.dwarf.game.GameService;
import org.springframework.samples.dwarf.location.Location;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.dwarf.mainboard.MainBoard;
import org.springframework.samples.dwarf.mainboard.MainBoardService;
import org.springframework.samples.dwarf.player.Player;
import org.springframework.samples.dwarf.player.PlayerRepository;
import org.springframework.samples.dwarf.user.UserRepository;
import org.springframework.samples.dwarf.user.UserService;
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
    void testTurnWhenSpecialCardUsed(){

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

        System.out.println(returnedPlayers);

        List<Player> expected = List.of(owner1,owner1,owner3,owner2);
        // Tanto el owner3 como el owner2 han tirado una carta especial con un solo dwarf
        // Por lo tanto tienen que tirar su turno al final de la ronda en sentido contrario

        assertEquals(returnedPlayers.get(0).getName(), starter.getName());
        assertEquals(returnedPlayers, expected);

    }
}
