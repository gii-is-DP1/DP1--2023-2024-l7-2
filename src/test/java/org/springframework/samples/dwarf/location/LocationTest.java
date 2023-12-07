package org.springframework.samples.dwarf.location;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.location.Location;

public class LocationTest {

    private Location location;

    @BeforeEach
    public void setUp() {
        location = new Location();
    }

    @Test
    public void testGetPosition() {
        location.setPosition(5);
        assertEquals(5, location.getPosition());
    }

    @Test
    public void testSetPosition() {
        location.setPosition(3);
        assertEquals(3, location.getPosition());
    }

    @Test
    public void testGetCards() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card());
        cards.add(new Card());

        location.setCards(cards);

        assertEquals(cards, location.getCards());
    }

    @Test
    public void testSetCards() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card());
        cards.add(new Card());

        location.setCards(cards);

        assertEquals(cards, location.getCards());
    }



}
