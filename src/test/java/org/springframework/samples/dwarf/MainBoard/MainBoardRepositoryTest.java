package org.springframework.samples.dwarf.MainBoard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.dwarf.mainboard.MainBoard;
import org.springframework.samples.dwarf.mainboard.MainBoardRepository;

@DataJpaTest
public class MainBoardRepositoryTest {

    @Autowired
    private MainBoardRepository mainBoardRepository;

    @MockBean
    private MainBoardRepository mockMainBoardRepository;

    @BeforeEach
    public void setUp() {
        MainBoard mainBoard1 = new MainBoard();
        mainBoard1.setId(1);
        MainBoard mainBoard2 = new MainBoard();
        mainBoard2.setId(2);

        List<MainBoard> mainBoards = Arrays.asList(mainBoard1, mainBoard2);
        when(mockMainBoardRepository.findAll()).thenReturn(mainBoards);
    }

    @Test
    public void testFindAll() {
        List<MainBoard> mainBoards = mainBoardRepository.findAll();

        assertEquals(2, mainBoards.size());
    }

}
