package org.springframework.samples.petclinic.object;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.object.ObjectRepository;
import org.springframework.samples.petclinic.object.Object;

@DataJpaTest
public class ObjectRepositoryTest {

    @Autowired
    private ObjectRepository or;

    @Test
    public void testFindAll() {
        List<Object> objects = or.findAll();
        assertEquals(8, objects.size());
    }

    @Test
    public void testFindByName() {
        Object existingObject = or.findAll().get(0);
        String objectName = existingObject.getName();

        Object object = or.findByName(objectName);

        assertEquals(objectName, object.getName());
    }

}
