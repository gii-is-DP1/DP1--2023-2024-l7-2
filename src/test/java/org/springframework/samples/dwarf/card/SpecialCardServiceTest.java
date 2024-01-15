package org.springframework.samples.dwarf.card;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.dwarf.dwarf.Dwarf;
import org.springframework.samples.dwarf.dwarf.DwarfService;
import org.springframework.samples.dwarf.game.Game;
import org.springframework.samples.dwarf.game.GameService;
import org.springframework.samples.dwarf.location.Location;
import org.springframework.samples.dwarf.location.LocationService;
import org.springframework.samples.dwarf.mainboard.MainBoardService;
import org.springframework.samples.dwarf.object.Object;
import org.springframework.samples.dwarf.player.Player;
import org.springframework.samples.dwarf.player.PlayerService;
import org.springframework.test.context.junit4.SpringRunner;

import jakarta.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class SpecialCardServiceTest {

    @Autowired
    private PlayerService pService;

    @Autowired
    private DwarfService dwService;

    @Autowired
    private SpecialCardRepository repo;

    @Autowired
    private LocationService locService;

    @Autowired
    private MainBoardService mbService;

    @Autowired
    private GameService gService;

    @Autowired
    private SpecialCardService specCardService;

    // @Autowired
    // public SpecialCardServiceTest(SpecialCardService specCardService) {
    // this.specCardService = specCardService;
    // }
    /*
     * @BeforeEach
     * public void setup() {
     * specCardService = new SpecialCardService(repo, dwService, pService,
     * locService, mbService);
     * }
     */

    @Test
    @Transactional
    void shouldAddTwoDwarves() {
        Game g = this.gService.getGameByCode("test-code");
        Player p = g.getPlayers().get(2);
        Boolean usesBothDwarves = true;
        Integer round = 1;

        Integer currentSize = g.getDwarves().size();

        g = specCardService.handleIfBothDwarvesAreUsed(g, p, round, usesBothDwarves);
        List<Dwarf> dwarves = g.getDwarves();

        assertEquals(dwarves.size() - currentSize, 2);
        assertEquals(dwarves.get(dwarves.size() - 1).getPlayer(), p);
    }

    @Test
    @Transactional
    void shouldAddOneDwarf() {
        Game g = this.gService.getGameByCode("test-code");
        Player p = g.getPlayers().get(2);
        Boolean usesBothDwarves = false;
        Integer round = 1;

        Integer currentSize = g.getDwarves().size();
        Integer currentMedals = p.getMedal();

        g = specCardService.handleIfBothDwarvesAreUsed(g, p, round, usesBothDwarves);
        List<Dwarf> dwarves = g.getDwarves();

        assertEquals(dwarves.size() - currentSize, 1);
        assertEquals(dwarves.get(dwarves.size() - 1).getPlayer(), p);

        p = g.getPlayers().get(2);
        assertEquals(currentMedals - p.getMedal(), 4);
    }

    @Test
    @Transactional
    void musterAnArmyActionTest() {
        Game g = this.gService.getGameByCode("test-code");

        List<Card> cards = g.getMainBoard().getCards();
        Integer round = 1;

        Integer currentDwarvesSize = g.getDwarves().size();

        // when(dwService.saveDwarf(any(Dwarf.class))).thenReturn(new Dwarf());

        g = specCardService.musterAnArmyAction(g, round, cards);
        List<Dwarf> dwarves = g.getDwarves();

        assertEquals(dwarves.size() - currentDwarvesSize, 1);

        assertEquals(dwarves.get(dwarves.size() - 1).getPlayer(), null);

    }

    @Test
    @Transactional
    void apprenticeActionTest() {
        Game g = this.gService.getGameByCode("test-code");
        Player p = g.getPlayers().get(2);
        List<Dwarf> dwarves = g.getDwarves();
        Integer round = 1;
        Integer selectedPosition = 1;

        Integer currentDwarvesSize = g.getDwarves().size();

        g = specCardService.apprenticeAction(g, p, round, selectedPosition, dwarves);
        assertEquals(dwarves.size() - currentDwarvesSize, 1);

        assertEquals(dwarves.get(dwarves.size() - 1).getPlayer(), p);
        assertEquals(dwarves.get(dwarves.size() - 1).getCard().getPosition(), selectedPosition);
    }

    @Test
    @Transactional
    void specialOrderTest() {
        Game g = this.gService.getGameByCode("test-code");
        Player p = g.getPlayers().get(1);
        Object o = g.getPlayers().get(2).getObjects().get(3);

        Integer currentGold = p.getGold();
        Integer currentIron = p.getIron();
        Integer currentSteal = p.getSteal();

        Integer selectedGold = 3;
        Integer selectedIron = 1;
        Integer selectedSteal = 1;

        specCardService.specialOrderAction(p, selectedGold, selectedIron, selectedSteal, o);

        p = pService.getById(p.getId());
        assertEquals((Integer) (currentGold - p.getGold()), selectedGold);
        assertEquals((Integer) (currentIron - p.getIron()), selectedIron);
        assertEquals((Integer) (currentSteal - p.getSteal()), selectedSteal);
        assertEquals(p.getObjects().contains(o), true);
    }

    @Test
    @Transactional
    void sellAnItemTest() {
        Game g = this.gService.getGameByCode("test-code");
        Player p = g.getPlayers().get(2);
        Object o = g.getPlayers().get(2).getObjects().get(3);

        Integer currentGold = p.getGold();
        Integer currentIron = p.getIron();
        Integer currentSteal = p.getSteal();

        Integer selectedGold = 3;
        Integer selectedIron = 1;
        Integer selectedSteal = 1;

        specCardService.sellAnItemAction(p, selectedGold, selectedIron, selectedSteal, o);

        p = pService.getById(p.getId());
        assertEquals(p.getGold(), (Integer) (currentGold + selectedGold));
        assertEquals(p.getIron(), (Integer) (currentIron + selectedIron));
        assertEquals(p.getSteal(), (Integer) (currentSteal + selectedSteal));
        assertEquals(p.getObjects().contains(o), false);
    }

    @Test
    @Transactional
    void turnBackActionTest() {
        Game g = this.gService.getGameByCode("test-code");
        Player p = g.getPlayers().get(2);
        Integer round = 1;
        Integer selectedPosition = 1;
        List<Location> locations = g.getMainBoard().getLocations();

        Integer currentDwarvesSize = g.getDwarves().size();

        g = specCardService.turnBackAction(g, p, round, selectedPosition, locations);

        List<Dwarf> dwarves = g.getDwarves();
        assertEquals(dwarves.size() - currentDwarvesSize, 1);

        Card c = g.getMainBoard().getCards().get(0);
        Dwarf d = dwarves.get(dwarves.size() - 1);
        assertEquals(d.getCard(), c);
        assertEquals(d.getPlayer(), p);
    }
}
