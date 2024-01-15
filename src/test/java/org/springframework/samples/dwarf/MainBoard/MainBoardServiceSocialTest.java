package org.springframework.samples.dwarf.MainBoard;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.CardService;
import org.springframework.samples.dwarf.card.CardType;
import org.springframework.samples.dwarf.card.SpecialCard;
import org.springframework.samples.dwarf.card.SpecialCardRepository;
import org.springframework.samples.dwarf.card.SpecialCardService;
import org.springframework.samples.dwarf.cardDeck.CardDeck;
import org.springframework.samples.dwarf.cardDeck.CardDeckService;
import org.springframework.samples.dwarf.dwarf.Dwarf;
import org.springframework.samples.dwarf.game.Game;
import org.springframework.samples.dwarf.game.GameService;
import org.springframework.samples.dwarf.location.Location;
import org.springframework.samples.dwarf.location.LocationService;
import org.springframework.samples.dwarf.mainboard.MainBoard;
import org.springframework.samples.dwarf.mainboard.MainBoardRepository;
import org.springframework.samples.dwarf.mainboard.MainBoardService;
import org.springframework.samples.dwarf.player.Player;
import org.springframework.test.context.junit4.SpringRunner;

import jakarta.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class MainBoardServiceSocialTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private MainBoardService mainBoardService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    void faseResolucionAccionesTest1() {
        Game g = gameService.getGameByCode("test-code");

        mainBoardService.faseResolucionAcciones(g);


        List<Player> plys = g.getPlayers();
        assertEquals(3, plys.get(0).getIron());
        assertEquals(0, plys.get(0).getSteal());
        assertEquals(1, plys.get(0).getGold());
        assertEquals(9, plys.get(1).getIron());
        assertEquals(8, plys.get(1).getSteal());
        assertEquals(7, plys.get(1).getGold());
        assertEquals(13, plys.get(2).getIron());
        assertEquals(50, plys.get(2).getSteal());
        assertEquals(20, plys.get(2).getGold());
    }
}
