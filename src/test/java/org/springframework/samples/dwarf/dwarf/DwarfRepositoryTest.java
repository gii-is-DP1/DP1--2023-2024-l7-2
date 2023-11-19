package org.springframework.samples.dwarf.dwarf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.dwarf.dwarf.Dwarf;
import org.springframework.samples.dwarf.dwarf.DwarfRepository;

public class DwarfRepositoryTest {

    private DwarfRepository dwarfRepository;

    @BeforeEach
    public void setUp() {
        dwarfRepository = mock(DwarfRepository.class);
    }

    @Test
    public void testFindAll() {
        // Configurar el comportamiento del repositorio mock
        List<Dwarf> mockDwarfs = new ArrayList<>();
        when(dwarfRepository.findAll()).thenReturn(mockDwarfs);

        // Ejecutar el método del repositorio y verificar el resultado
        List<Dwarf> result = (List<Dwarf>) dwarfRepository.findAll();
        assertEquals(mockDwarfs, result, "La lista de enanos no coincide");
    }

}