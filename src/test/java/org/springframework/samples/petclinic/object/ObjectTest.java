package org.springframework.samples.petclinic.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class ObjectTest {

    @Test
    public void testObjectCreation() {
        Object object = new Object();
        assertNotNull(object);
    }

    @Test
    public void testSetName() {
        Object object = new Object();
        String testName = "Test Object";
        object.setName(testName);
        assertEquals(testName, object.getName());
    }

    @Test
    public void testSetPhoto() {
        Object object = new Object();
        String testPhoto = "test-photo.jpg";
        object.setPhoto(testPhoto);
        assertEquals(testPhoto, object.getPhoto());
    }
   
}
