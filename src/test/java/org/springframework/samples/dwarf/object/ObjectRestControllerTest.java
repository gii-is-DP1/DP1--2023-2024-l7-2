package org.springframework.samples.dwarf.object;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.dwarf.exceptions.ResourceNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ObjectRestControllerTest {

    @Mock
    private ObjectService os;

    @InjectMocks
    private ObjectRestController oc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        List<Object> testObjects = new ArrayList<>();
        when(os.getObjects()).thenReturn(testObjects);
        ResponseEntity<List<Object>> response = oc.findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testObjects, response.getBody());
        verify(os, times(1)).getObjects();
    }

    @Test
    public void testFindObject() {
        Object testObject = new Object();
        when(os.getById(1)).thenReturn(testObject);

        ResponseEntity<Object> response = oc.findObject(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testObject, response.getBody());

        verify(os, times(1)).getById(1);

        when(os.getById(2)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> oc.findObject(2));
    }

}
