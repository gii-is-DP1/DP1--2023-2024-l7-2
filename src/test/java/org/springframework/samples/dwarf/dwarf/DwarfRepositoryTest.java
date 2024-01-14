package org.springframework.samples.dwarf.dwarf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DwarfRepositoryTest {

    @Autowired
    private DwarfRepository dwarfRepository;

    @Test
    public void testFindAll() {

        // Ejecutar el método del repositorio y verificar el resultado
        List<Dwarf> result =  dwarfRepository.findAll();
        assertEquals(24, result.size(), "La lista de enanos no coincide");
    }

}