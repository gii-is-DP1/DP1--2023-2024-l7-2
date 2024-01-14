package org.springframework.samples.dwarf.dwarf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DwarfRepositoryTest {

    @Mock
    private DwarfRepository dwarfRepository;

    @Test
    public void testFindAll_Positive() {
        // Mocking behavior of the repository
        List<Dwarf> mockDwarfs = Arrays.asList(new Dwarf(), new Dwarf());
        when(dwarfRepository.findAll()).thenReturn(mockDwarfs);

        // Calling the method
        List<Dwarf> dwarfs = dwarfRepository.findAll();

        // Verifying interactions and assertions
        verify(dwarfRepository, times(1)).findAll();
        assertNotNull(dwarfs);
        assertEquals(2, dwarfs.size());
    }

    @Test
    public void testFindAll_EmptyList_Negative() {
        // Mocking behavior of the repository
        when(dwarfRepository.findAll()).thenReturn(Collections.emptyList());

        // Calling the method
        List<Dwarf> dwarfs = dwarfRepository.findAll();

        // Verifying interactions and assertions
        verify(dwarfRepository, times(1)).findAll();
        assertNotNull(dwarfs);
        assertTrue(dwarfs.isEmpty());
    }

}