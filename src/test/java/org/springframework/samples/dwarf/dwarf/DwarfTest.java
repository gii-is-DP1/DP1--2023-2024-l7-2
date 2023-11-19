package org.springframework.samples.dwarf.dwarf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.dwarf.Dwarf;
import org.springframework.samples.dwarf.dwarf.DwarfRepository;
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
        dwarf.setName("Gimli");
        dwarf.setRound(1);

        Player player = new Player();
        player.setName("Aragorn");
        dwarf.setPlayer(player);

        List<Card> cards = new ArrayList<>();
        Card card1 = new Card();
        cards.add(card1);
        dwarf.setCards(cards);
    }

    @Test
    public void saveDwarfWithPlayerAndCards() {
        dwarfRepository.save(dwarf);

        List<Card> cards = new ArrayList<>();
        Card card1 = new Card();
        cards.add(card1);

        Dwarf savedDwarf = new Dwarf();
        Player jugador = new Player();
        savedDwarf.setName("Pepe");
        savedDwarf.setCards(cards);
        jugador.setName("Aragorn");
        savedDwarf.setRound(1);
        savedDwarf.setPlayer(jugador);
        assertNotNull(savedDwarf);
        assertEquals("Pepe", savedDwarf.getName());
        assertEquals(1, savedDwarf.getRound().intValue());
        assertNotNull(savedDwarf.getPlayer());
        assertEquals("Aragorn", savedDwarf.getPlayer().getName());
        assertNotNull(savedDwarf.getCards());
        assertEquals(1, savedDwarf.getCards().size());
    }

    @Test
    public void saveDwarfWithoutPlayerAndCards() {
        dwarf.setPlayer(null);
        dwarf.setCards(null);

        dwarfRepository.save(dwarf);

        Dwarf savedDwarf = new Dwarf();
        assertNotNull(savedDwarf);
        assertNull(savedDwarf.getPlayer());
        assertNull(savedDwarf.getCards());
    }
}
