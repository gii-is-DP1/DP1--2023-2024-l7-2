package org.springframework.samples.dwarf.dwarf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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

        // Ejecutar el método del repositorio y verificar el resultado
        List<Dwarf> result = dwarfRepository.findAll();
        assertEquals(29, result.size(), "La lista de enanos no coincide");
    }

}