package org.springframework.samples.dwarf.location;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.dwarf.location.Location;
import org.springframework.samples.dwarf.location.LocationRepository;

public class LocationRepositoryTest {

    private LocationRepository locationRepository;

    @BeforeEach
    public void setUp() {
        locationRepository = mock(LocationRepository.class);
    }

    @Test
    public void testFindAll() {
        List<Location> locations = new ArrayList<>();
        locations.add(new Location());
        locations.add(new Location());

        when(locationRepository.findAll()).thenReturn(locations);

        List<Location> result = locationRepository.findAll();

        assertEquals(locations.size(), result.size());
        assertEquals(locations, result);
    }

    @Test
    public void testFindById() {
        Location location = new Location();
        location.setId(1);

        when(locationRepository.findById(1)).thenReturn((location));

        Optional<Location> result = Optional.ofNullable(locationRepository.findById(1));

        assertTrue(result.isPresent());
        assertEquals(location.getId(), result.get().getId());
    }

    @Test
    public void testFindById_NotFound() {
        when(locationRepository.findById(1)).thenReturn((null));


        Optional<Location> result = Optional.ofNullable(locationRepository.findById(1));

        assertFalse(result.isPresent());
    }
}
