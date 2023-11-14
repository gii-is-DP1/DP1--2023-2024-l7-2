package org.springframework.samples.petclinic.object;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.exceptions.BadRequestException;
import org.springframework.samples.petclinic.exceptions.ResourceNotFoundException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ObjectRestControllerTest {

    @Mock
    private ObjectService objectService;

    @InjectMocks
    private ObjectRestController objectRestController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {

        when(objectService.getObjects()).thenReturn(Arrays.asList(new Object(), new Object()));
      
        ResponseEntity<List<Object>> responseEntity = objectRestController.findAll();
    
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().size());
    }

    @Test
    public void testFindObject() {
        
        when(objectService.getById(1)).thenReturn(new Object());
        ResponseEntity<Object> responseEntity = objectRestController.findObject(1);

       
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testFindObjectNotFound() {
      
        when(objectService.getById(1)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> objectRestController.findObject(1));
    }

    @Test
    public void testCreateObject() {
       
        when(objectService.saveObject(any())).thenReturn(new Object());

       
        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "object");
        ResponseEntity<Object> responseEntity = objectRestController.createObject(new Object(), bindingResult);

      
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testCreateObjectWithValidationError() {
        when(objectService.saveObject(any())).thenReturn(new Object());

        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "object");
        bindingResult.reject("testError");
        assertThrows(BadRequestException.class, () -> objectRestController.createObject(new Object(), bindingResult));
    }


    @Test
    public void testModifyObjectWithValidationError() {
        
        when(objectService.getById(1)).thenReturn(new Object());

        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "object");
        bindingResult.reject("testError");
        assertThrows(BadRequestException.class, () -> objectRestController.modifyObject(new Object(), bindingResult, 1));
    }

    @Test
    public void testModifyObjectInconsistentId() {
        
        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "object");
        assertThrows(ResourceNotFoundException.class, 
                () -> objectRestController.modifyObject(new Object(), bindingResult, 2));
    }

    @Test
    public void testDeleteObject() {
      
        when(objectService.getById(1)).thenReturn(new Object());
        doNothing().when(objectService).deleteObjectById(1);

       
        ResponseEntity<Void> responseEntity = objectRestController.deleteObject(1);

       
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteObjectNotFound() {
        
        when(objectService.getById(1)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> objectRestController.deleteObject(1));
    }
}
