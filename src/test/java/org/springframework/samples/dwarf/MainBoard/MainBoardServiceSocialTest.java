package org.springframework.samples.dwarf.MainBoard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.dwarf.game.Game;
import org.springframework.samples.dwarf.game.GameService;
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
