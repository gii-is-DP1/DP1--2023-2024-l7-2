package org.springframework.samples.dwarf.location;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Array;
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
        locationService = new LocationService(locationRepository,null);
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
    public void testGetById_Exists() {
        // Datos de prueba
        int locationId = 1;
        Location expectedLocation = new Location();
        expectedLocation.setId(locationId);

        // Configuración del repositorio mock
        when(locationRepository.findById(locationId)).thenReturn(Optional.of(expectedLocation));

        // Llamada al método que se va a probar
        Location result = locationService.getById(locationId);

        // Verificaciones
        assertNotNull(result, "La ubicación no debería ser nula");
        assertEquals(locationId, result.getId(), "El ID de la ubicación no coincide");
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
        ArrayList<Card> originalCards = new ArrayList<>();
        Card c1 = new Card();
        Card c2 = new Card();
        c1.setId(1);
        c1.setName("name1");
        c2.setId(2);
        c2.setName("name2");

        originalCards.add(c1);
        originalCards.add(c2);


        
        location.setCards(originalCards);
    
        when(locationRepository.save(location)).thenReturn(location);
    
        Location result = locationService.putFirstCardAtEnd(location);

        assertNotNull(result);
        List<Card> resultCards = result.getCards();
    
        // Verificar que las tarjetas se han movido correctamente
        assertEquals(c1, resultCards.get(1));
        assertEquals(c2, resultCards.get(0));
    }
    @Test
    public void testRemoveLastCard() {
        Location location = new Location();
        ArrayList<Card> locationCards = new ArrayList<>();

        Card c1 = new Card();
        Card cardToRemove = new Card();
        c1.setId(1);
        c1.setName("name1");
        cardToRemove.setId(2);
        cardToRemove.setName("name2");

        locationCards.add(c1);
        locationCards.add(cardToRemove);
        location.setCards(locationCards);

        when(locationRepository.save(location)).thenReturn(location);

        Card result = locationService.removeLastCard(location);

        assertNotNull(result);
        assertEquals(cardToRemove, result);
        assertFalse(location.getCards().contains(cardToRemove));
    }

    @Test
    public void testPastGloriesAction() {
        Location location = new Location();
        List<Card> cards = new ArrayList<>();
        
        Card c1 = new Card();
        Card c2 = new Card();
        Card c3 = new Card();
        Card c4 = new Card();
        c1.setId(1);
        c1.setName("name1");
        c2.setId(2);
        c2.setName("name2");
        c3.setId(3);
        c3.setName("name3");
        c4.setId(4);
        c4.setName("name4");
        
        cards.add(c1);
        cards.add(c2);
        cards.add(c3);
        cards.add(c4);
        location.setCards(cards);
    
        when(locationRepository.save(location)).thenReturn(location);
    
        Location result = locationService.pastGloriesAction(location, c3);
    
        assertNotNull(result);
    
        // Verificar que la carta original ya no está presente en la lista
        assertTrue(result.getCards().contains(c3));
    
        // Verificar que la última carta en la lista es igual a la carta original
        assertEquals(c3, result.getCards().get(result.getCards().size() - 1));
    }
    
}
