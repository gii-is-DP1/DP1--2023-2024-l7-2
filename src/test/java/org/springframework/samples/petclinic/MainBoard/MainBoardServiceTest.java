package org.springframework.samples.petclinic.MainBoard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.CardService;
import org.springframework.samples.petclinic.card.SpecialCard;
import org.springframework.samples.petclinic.cardDeck.CardDeck;
import org.springframework.samples.petclinic.cardDeck.CardDeckService;
import org.springframework.samples.petclinic.mainboard.MainBoard;
import org.springframework.samples.petclinic.mainboard.MainBoardRepository;
import org.springframework.samples.petclinic.mainboard.MainBoardService;
import org.springframework.samples.petclinic.specialCardDeck.SpecialCardDeck;
import org.springframework.samples.petclinic.specialCardDeck.SpecialCardDeckService;

public class MainBoardServiceTest {

    @Mock
    private MainBoardRepository mainBoardRepository;

    @Mock
    private CardDeckService cardDeckService;

    @Mock
    private SpecialCardDeckService specialCardDeckService;

    @Mock
    private CardService cardService;

    @InjectMocks
    private MainBoardService mainBoardService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetMainBoard() {
        when(mainBoardRepository.findAll()).thenReturn(Arrays.asList(new MainBoard(), new MainBoard()));

        List<MainBoard> mainBoards = mainBoardService.getMainBoard();

        assertEquals(2, mainBoards.size());
    }

    @Test
    public void testSaveMainBoard() {
        MainBoard mainBoard = new MainBoard();

        when(mainBoardRepository.save(any())).thenReturn(mainBoard);

        MainBoard savedMainBoard = mainBoardService.saveMainBoard(mainBoard);

        assertNotNull(savedMainBoard);
    }

    @Test
    public void testGetById() {
        MainBoard mainBoard = new MainBoard();
        mainBoard.setId(1);

        when(mainBoardRepository.findById(1)).thenReturn(Optional.of(mainBoard));

        MainBoard retrievedMainBoard = mainBoardService.getById(1);

        assertNotNull(retrievedMainBoard);
        assertEquals(1, retrievedMainBoard.getId());
    }

    @Test
    public void testInitialize() {
        CardDeck cardDeck = mock(CardDeck.class);
        when(cardDeckService.initialiate()).thenReturn(cardDeck);

        Card card1 = new Card();
        card1.setId(1);
        when(cardService.getById(1)).thenReturn(card1);

        Card card2 = new Card();
        card2.setId(2);
        when(cardService.getById(2)).thenReturn(card2);

        MainBoard initializedMainBoard = mainBoardService.initialize();

        assertNotNull(initializedMainBoard);
        assertEquals(cardDeck, initializedMainBoard.getCardDeck());
        assertEquals(9, initializedMainBoard.getCards().size());
    }

  
}
