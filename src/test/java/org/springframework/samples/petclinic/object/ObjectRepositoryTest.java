package org.springframework.samples.petclinic.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ObjectRepositoryTest {

    @Autowired
    private ObjectRepository objectRepository;

    private Object testObject;

     @BeforeEach
    public void setUp() {
        testObject = new Object();
        testObject.setName("Test Object");
        testObject.setPhoto("test-photo.jpg");
    }

    @Test
    public void testFindAll() {
        List<Object> objects = objectRepository.findAll();
        assertNotNull(objects);
    }

    @Test
    public void testFindByName() {
   
        objectRepository.save(testObject);
        String objectName = testObject.getName();

        Object foundObject = objectRepository.findByName(objectName);

        assertNotNull(foundObject);
        assertEquals(objectName, foundObject.getName());
        
    }

    
}
