package org.springframework.samples.dwarf.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.dwarf.object.Object;;

@SpringBootTest
public class ObjectTest {

    @Test
    public void testObjectProperties() {
        Object object = new Object();
        object.setName("Wooden sword");
        object.setPhoto("src/main/resources/static/objects/maze.png");

        // Asegúrate de que las propiedades se hayan establecido correctamente
        assertEquals("Wooden sword", object.getName());
        assertEquals("src/main/resources/static/objects/maze.png", object.getPhoto());
    }

}
