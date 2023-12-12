package org.springframework.samples.dwarf.dwarf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.player.Player;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DwarfTest {

    @Autowired
    private DwarfRepository dwarfRepository;

    private Dwarf dwarf;

    @Before
    public void setUp() {
        dwarf = new Dwarf();
        dwarf.setRound(1);

        Player player = new Player();
        player.setName("Aragorn");
        dwarf.setPlayer(player);

        Card card = new Card();
        dwarf.setCard(card);
    }

    @Test
    public void saveDwarfWithPlayerAndCards() {


        Card card = new Card();

        Dwarf savedDwarf = new Dwarf();
        Player jugador = new Player();
        savedDwarf.setCard(card);
        jugador.setName("Aragorn");
        savedDwarf.setRound(1);
        savedDwarf.setPlayer(jugador);
        assertNotNull(savedDwarf);
        assertEquals(1, savedDwarf.getRound().intValue());
        assertNotNull(savedDwarf.getPlayer());
        assertEquals("Aragorn", savedDwarf.getPlayer().getName());
        assertNotNull(savedDwarf.getCard());
    }

    @Test
    public void saveDwarfWithoutPlayerAndCards() {
        dwarf.setPlayer(null);
        dwarf.setCard(null);

        dwarfRepository.save(dwarf);

        Dwarf savedDwarf = new Dwarf();
        assertNotNull(savedDwarf);
        assertNull(savedDwarf.getPlayer());
        assertNull(savedDwarf.getCard());
    }
}
