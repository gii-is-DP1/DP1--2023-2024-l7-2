package org.springframework.samples.petclinic.specialCard;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.samples.petclinic.card.SpecialCard;
import org.springframework.samples.petclinic.card.SpecialCardRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class SpecialCardTest {

    @Test
    public void testSpecialCardGetterAndSetter() {
        // Crear una instancia de SpecialCard
        SpecialCard specialCard = new SpecialCard();
        specialCard.setId(1); // Usar 1 en lugar de 1L
        specialCard.setName("TestCard");
        specialCard.setDescription("TestDescription");
    
        // Verificar que los getter devuelvan los valores correctos
        assertEquals(Integer.valueOf(1), specialCard.getId()); 
        assertEquals("TestCard", specialCard.getName());
        assertEquals("TestDescription", specialCard.getDescription());
    
        // Verificar que los setter funcionen correctamente
        specialCard.setId(2);
        specialCard.setName("UpdatedTestCard");
        specialCard.setDescription("UpdatedTestDescription");
    
        assertEquals(Integer.valueOf(2), specialCard.getId()); 
        assertEquals("UpdatedTestCard", specialCard.getName());
        assertEquals("UpdatedTestDescription", specialCard.getDescription());
    }

    @Test
    public void testSpecialCardToJson() throws Exception {
        // Crear una instancia de SpecialCard
        SpecialCard specialCard = new SpecialCard();
        specialCard.setId(1);
        specialCard.setName("TestCard");
        specialCard.setDescription("TestDescription");

        // Convertir SpecialCard a JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(specialCard);

        // Verificar que el JSON resultante tenga los valores esperados
        assertEquals("{\"id\":1,\"name\":\"TestCard\",\"description\":\"TestDescription\"}", json);
    }

    @Test
    public void testSpecialCardRepositorySave() {
        // Mock del repositorio
        SpecialCardRepository repositoryMock = mock(SpecialCardRepository.class);

        // Crear una instancia de SpecialCard
        SpecialCard specialCard = new SpecialCard();
        specialCard.setId(1);
        specialCard.setName("TestCard");
        specialCard.setDescription("TestDescription");

        // Llamar al método save del repositorio
        repositoryMock.save(specialCard);

        // Verificar que se haya llamado a save con la instancia correcta
        verify(repositoryMock, times(1)).save(specialCard);
    }
}