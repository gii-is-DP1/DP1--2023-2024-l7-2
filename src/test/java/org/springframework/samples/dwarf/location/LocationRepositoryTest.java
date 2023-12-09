package org.springframework.samples.dwarf.location;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.dwarf.player.Player;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class LocationRepositoryTest {

    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void testFindAll() {
        // Given
        Location l1 = new Location();
        l1.setPosition(1); // Establece un valor no nulo para position
        locationRepository.save(l1);

        Location l2 = new Location();
        l2.setPosition(2); // Establece un valor no nulo para position
        locationRepository.save(l2);

        // When
        List<Location> locations = locationRepository.findAll();

        // Then
        assertEquals(2, locations.size());
        assertTrue(locations.contains(l1));
        assertTrue(locations.contains(l2));
    }
    @Test
    public void testFindById_Exists() {
        // Agrega una ubicación de prueba al repositorio
        Location location = new Location();
        Location savedLocation = locationRepository.save(location);

        // Llama al método que se va a probar
        Optional<Location> result = locationRepository.findById(savedLocation.getId());

        // Verificaciones
        assertTrue(result.isPresent(), "La ubicación debería existir");
        assertEquals(savedLocation.getId(), result.get().getId(), "El ID de la ubicación no coincide");
    }

    @Test
    public void testFindById_NotExists() {
        // Llama al método que se va a probar con un ID que no existe
        Optional<Location> result = locationRepository.findById(999);

        // Verificaciones
        assertTrue(result.isEmpty(), "La ubicación no debería existir");
    }
}
