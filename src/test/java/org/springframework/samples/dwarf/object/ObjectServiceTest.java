package org.springframework.samples.dwarf.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.dwarf.object.Object;
import org.springframework.samples.dwarf.object.ObjectService;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ObjectServiceTest {

    @Autowired
    private ObjectService os;

    @Test
    public void testGetObjects() {
        List<Object> objects = os.getObjects();
        assertNotNull(objects);

    }

    @Test
    public void testGetById() {

        Object existingObject = os.getObjects().get(0);

        Object retrievedObject = os.getById(existingObject.getId());
        assertNotNull(retrievedObject);
        assertEquals(existingObject.getId(), retrievedObject.getId());

    }

    @Test
    public void testGetObjectByName() {

        Object existingObject = os.getObjects().get(0);
        String objectName = existingObject.getName();

        Object retrievedCard = os.getObjectByName(objectName);

        assertNotNull(retrievedCard);
        assertEquals(objectName, retrievedCard.getName());

    }
}
