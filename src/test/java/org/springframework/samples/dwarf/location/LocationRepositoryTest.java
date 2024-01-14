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


        
        List<Location> locations = locationRepository.findAll();

        
        assertEquals(9, locations.size());
        
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
