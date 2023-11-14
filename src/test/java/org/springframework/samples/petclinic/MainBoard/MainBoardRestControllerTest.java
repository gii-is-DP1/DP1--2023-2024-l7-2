package org.springframework.samples.petclinic.MainBoard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.exceptions.ResourceNotFoundException;
import org.springframework.samples.petclinic.mainboard.MainBoard;
import org.springframework.samples.petclinic.mainboard.MainBoardRestController;
import org.springframework.samples.petclinic.mainboard.MainBoardService;

public class MainBoardRestControllerTest {

    @Mock
    private MainBoardService mainBoardService;

    @InjectMocks
    private MainBoardRestController mainBoardRestController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        when(mainBoardService.getMainBoard()).thenReturn(Arrays.asList(new MainBoard(), new MainBoard()));

        ResponseEntity<List<MainBoard>> responseEntity = mainBoardRestController.findAll();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().size());
    }

    @Test
    public void testGetMainBoard() {
        int id = 1;
        MainBoard mainBoard = new MainBoard();
        mainBoard.setId(id);

        when(mainBoardService.getById(id)).thenReturn(mainBoard);

        ResponseEntity<MainBoard> responseEntity = mainBoardRestController.getMainBoard(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(id, responseEntity.getBody().getId());
    }

    @Test
    public void testGetMainBoardNotFound() {
        int id = 1;

        when(mainBoardService.getById(id)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> mainBoardRestController.getMainBoard(id));
    }
}
