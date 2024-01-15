package org.springframework.samples.dwarf.MainBoard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.dwarf.mainboard.MainBoard;
import org.springframework.samples.dwarf.mainboard.MainBoardRepository;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase
@DataJpaTest
public class MainBoardRepositoryTest {

    @Autowired
    private MainBoardRepository mainBoardRepository;

    @BeforeEach
    public void setUp() {
        MainBoard mainBoard1 = new MainBoard();
        mainBoard1.setId(1);
        MainBoard mainBoard2 = new MainBoard();
        mainBoard2.setId(2);

        List<MainBoard> mainBoards = Arrays.asList(mainBoard1, mainBoard2);
    }

    @Test
    public void testFindAll() {
        List<MainBoard> mainBoards = mainBoardRepository.findAll();

        assertEquals(1, mainBoards.size());
    }

}
