package org.springframework.samples.petclinic.dwarf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.dwarf.Dwarf;
import org.springframework.samples.petclinic.dwarf.DwarfRestController;
import org.springframework.samples.petclinic.dwarf.DwarfService;
import org.springframework.samples.petclinic.exceptions.ResourceNotFoundException;
import org.springframework.validation.BindingResult;

@SpringBootTest
public class DwarfRestControllerTest {

    @Autowired
    private DwarfRestController dwarfRestController;

    @MockBean
    private DwarfService dwarfService;

    @Test
    public void testFindAll() {
        // Configurar el comportamiento del servicio mock
        List<Dwarf> mockDwarfs = new ArrayList<>();
        when(dwarfService.getDwarfs()).thenReturn(mockDwarfs);

        // Ejecutar el método del controlador y verificar el resultado
        ResponseEntity<List<Dwarf>> result = dwarfRestController.findAll();
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Código de estado incorrecto");
        assertEquals(mockDwarfs, result.getBody(), "La lista de enanos no coincide");
    }

    @Test
    public void testGetDwarfs_Success() {
        // Configurar el comportamiento del servicio mock
        Dwarf mockDwarf = new Dwarf();
        mockDwarf.setId(1);
        when(dwarfService.getById(1)).thenReturn(mockDwarf);

        // Ejecutar el método del controlador y verificar el resultado
        ResponseEntity<Dwarf> result = dwarfRestController.getDwarfs(1);
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Código de estado incorrecto");
        assertEquals(mockDwarf, result.getBody(), "El enano recuperado no coincide");
    }

    @Test
    public void testGetDwarfs_ResourceNotFound() {
        // Configurar el comportamiento del servicio mock para devolver null
        when(dwarfService.getById(1)).thenReturn(null);

        // Ejecutar el método del controlador y verificar que lanza una excepción de ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () -> dwarfRestController.getDwarfs(1));
    }



    

}