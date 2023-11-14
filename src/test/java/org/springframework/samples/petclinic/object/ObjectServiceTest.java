package org.springframework.samples.petclinic.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ObjectServiceTest {

    @Autowired
    private ObjectService objectService;

    private Object testObject;

    @BeforeEach
    public void setUp() {
        testObject = new Object();
        testObject.setName("Test Object");
        testObject.setPhoto("test-photo.jpg");
    }

    @Test
    public void testGetObjects() {
        List<Object> objects = objectService.getObjects();
        assertNotNull(objects);
        
    }

    @Test
    public void testGetById() {
        Object savedObject = objectService.saveObject(testObject);

        Object retrievedObject = objectService.getById(savedObject.getId());
        assertNotNull(retrievedObject);
        assertEquals(savedObject.getId(), retrievedObject.getId());
        
    }

    @Test
    public void testSaveObject() {
       
        Object savedObject = objectService.saveObject(testObject);

        assertNotNull(savedObject.getId());
       
    }

    @Test
    @DirtiesContext
    public void testDeleteObjectById() {
        Object savedObject = objectService.saveObject(testObject);
        int testObjectId = savedObject.getId();

        
        objectService.deleteObjectById(testObjectId);

      
        assertNull(objectService.getById(testObjectId));
    }

    @Test
    public void testGetObjectByName() {
      
        Object savedObject = objectService.saveObject(testObject);
        String objectName = savedObject.getName();

      
        Object retrievedObject = objectService.getObjectByName(objectName);

        assertNotNull(retrievedObject);
        assertEquals(objectName, retrievedObject.getName());
        
    }
}
