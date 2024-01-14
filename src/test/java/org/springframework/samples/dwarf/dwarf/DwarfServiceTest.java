package org.springframework.samples.dwarf.dwarf;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.dwarf.card.Card;

public class DwarfServiceTest {

    private DwarfRepository dwarfRepository;
    private DwarfService dwarfService;

    @BeforeEach
    public void setUp() {
        dwarfRepository = mock(DwarfRepository.class);
        dwarfService = new DwarfService(dwarfRepository);
    }

    @Test
    public void testGetDwarfs() {
        // Configurar el comportamiento del repositorio mock
        List<Dwarf> mockDwarfs = new ArrayList<>();
        when(dwarfRepository.findAll()).thenReturn(mockDwarfs);

        // Ejecutar el método del servicio y verificar el resultado
        List<Dwarf> result = dwarfService.getDwarfs();
        assertEquals(mockDwarfs, result, "La lista de enanos no coincide");
    }

    @Test
    public void testSaveDwarf() {
        // Configurar el comportamiento del repositorio mock
        Dwarf newDwarf = new Dwarf();
        when(dwarfRepository.save(newDwarf)).thenReturn(newDwarf);

        // Ejecutar el método del servicio y verificar el resultado
        Dwarf result = dwarfService.saveDwarf(newDwarf);
        assertEquals(newDwarf, result, "El enano guardado no coincide");
    }

    @Test
    public void testGetById_ExistingId() {
        // Configurar el comportamiento del repositorio mock
        int dwarfId = 1;
        Dwarf mockDwarf = new Dwarf();
        mockDwarf.setId(dwarfId);
        Optional<Dwarf> optionalDwarf = Optional.of(mockDwarf);
        when(dwarfRepository.findById(dwarfId)).thenReturn(optionalDwarf);

        // Ejecutar el método del servicio y verificar el resultado
        Dwarf result = dwarfService.getById(dwarfId);
        assertEquals(mockDwarf, result, "El enano recuperado no coincide");
    }

    @Test
    public void testGetById_NonExistingId() {
        // Configurar el comportamiento del repositorio mock para devolver
        // Optional.empty()
        int dwarfId = 1;
        when(dwarfRepository.findById(dwarfId)).thenReturn(Optional.empty());

        // Ejecutar el método del servicio y verificar que devuelve null
        Dwarf result = dwarfService.getById(dwarfId);
        assertNull(result, "Se esperaba null para un ID inexistente");
    }

    @Test
    public void testDeleteDwarf() {
        
        Dwarf dwarfToDelete = new Dwarf();

        
        dwarfService.deleteDwarf(dwarfToDelete);

        
        verify(dwarfRepository, times(1)).delete(dwarfToDelete);
    }

    @Test
    public void testDeleteDwarf_WithNullDwarf() {
        
        assertDoesNotThrow(() -> dwarfService.deleteDwarf(null));
    }

    @Test
    public void testUpdateDwarvesWhenUpdatedCards(){

        Card card1 = new Card();
        card1.setName("sample1");
        card1.setId(56);
        card1.setDescription("asdasd");
        card1.setPosition(1);
    
        Card card2 = new Card();
        card2.setName("sample2");
        card2.setId(57);
        card2.setDescription("asdasd");
        card2.setPosition(2);

        Card card3 = new Card();
        card3.setName("sample3");
        card3.setId(58);
        card3.setDescription("asdasd");
        card3.setPosition(2);

        Dwarf mockDwarf1 = new Dwarf();
        mockDwarf1.setId(1);
        mockDwarf1.setCard(card1);
        mockDwarf1.setRound(1);

        Dwarf mockDwarf2 = new Dwarf();
        mockDwarf2.setId(2);
        mockDwarf2.setCard(card2);
        mockDwarf2.setRound(1);

        List<Dwarf> dwarves = List.of(mockDwarf1,mockDwarf2);
        List<Card> cards = List.of(card1,card3);

        dwarfService.updateDwarvesWhenUpdatedCards(dwarves, cards);

        assertEquals(dwarves.get(0).getCard(), card1);
        assertEquals(dwarves.get(1).getCard(), card3);
    }
}