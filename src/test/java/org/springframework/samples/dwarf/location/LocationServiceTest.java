package org.springframework.samples.dwarf.location;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.location.Location;
import org.springframework.samples.dwarf.location.LocationRepository;

public class LocationServiceTest {

    private LocationService locationService;
    private LocationRepository locationRepository;

    @BeforeEach
    public void setUp() {
        locationRepository = mock(LocationRepository.class);
        locationService = new LocationService(locationRepository);
    }

    @Test
    public void testGetAll() {
        List<Location> locations = new ArrayList<>();
        locations.add(new Location());
        locations.add(new Location());

        when(locationRepository.findAll()).thenReturn(locations);

        List<Location> result = locationService.getAll();

        assertEquals(locations.size(), result.size());
        assertEquals(locations, result);
    }

 @Test
public void testGetById() {
    Location location = new Location();
    location.setId(1);


    Location result = locationService.getById(1);

    
    assertEquals(location.getId(), result.getId());
}

    @Test
    public void testSave() {
        Location location = new Location();
        location.setId(1);

        when(locationRepository.save(location)).thenReturn(location);

        Location result = locationService.save(location);

        assertNotNull(result);
        assertEquals(location.getId(), result.getId());
    }

    @Test
    public void testPushCard() {
        Location location = new Location();
        Card card = new Card();

        when(locationRepository.save(location)).thenReturn(location);

        Location result = locationService.pushCard(location, card);

        assertNotNull(result);
        assertTrue(result.getCards().contains(card));
    }

    @Test
    public void testShuffleLocation() {
        Location location = new Location();
        List<Card> originalCards = new ArrayList<>();
        Card c1 = new Card();
        Card c2 = new Card();
        originalCards.add(c1);
        originalCards.add(c2);
        location.setCards(originalCards);
    
        when(locationRepository.save(location)).thenReturn(location);
    
        Location result = locationService.shuffleLocation(location);
    
        assertNotNull(result);
        
        // Verificar que las propiedades de las cartas se han barajado
        for (Card originalCard : originalCards) {
            assertTrue(result.getCards().contains(originalCard));
        }
    }
    
    @Test
    public void testPutFirstCardAtEnd() {
        Location location = new Location();
        List<Card> originalCards = new ArrayList<>();
        Card c1 = new Card();
        Card c2 = new Card();
        originalCards.add(c1);
        originalCards.add(c2);
        location.setCards(originalCards);
    
        when(locationRepository.save(location)).thenReturn(location);
    
        Location result = locationService.putFirstCardAtEnd(location);
    
        assertNotNull(result);
    
        // Verificar que las tarjetas se han movido correctamente
        assertEquals(originalCards.get(0), result.getCards().get(result.getCards().size() - 1));
        assertEquals(originalCards.get(1), result.getCards().get(0));
    }
    @Test
    public void testRemoveFirstCard() {
        Location location = new Location();
        Card cardToRemove = new Card();
        List<Card> cards = new ArrayList<>();
        cards.add(cardToRemove);
        cards.add(new Card());
        location.setCards(cards);

        when(locationRepository.save(location)).thenReturn(location);

        Card result = locationService.removeFirstCard(location);

        assertNotNull(result);
        assertEquals(cardToRemove, result);
        assertFalse(location.getCards().contains(cardToRemove));
    }

    @Test
    public void testPastGloriesAction() {
        Location location = new Location();
        Card card = new Card();
        List<Card> cards = new ArrayList<>();
        cards.add(card);
        cards.add(new Card());
        location.setCards(cards);
    
        when(locationRepository.save(location)).thenReturn(location);
    
        Location result = locationService.pastGloriesAction(location, card);
    
        assertNotNull(result);
    
        // Verificar que la carta original ya no está presente en la lista
        assertFalse(result.getCards().contains(card));
    
        // Verificar que la última carta en la lista es igual a la carta original
        assertEquals(card, result.getCards().get(result.getCards().size() - 1));
    }
    
}
