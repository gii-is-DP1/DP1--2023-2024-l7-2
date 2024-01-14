package org.springframework.samples.dwarf.MainBoard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.CardService;
import org.springframework.samples.dwarf.card.SpecialCardRepository;
import org.springframework.samples.dwarf.card.SpecialCardService;
import org.springframework.samples.dwarf.cardDeck.CardDeck;
import org.springframework.samples.dwarf.cardDeck.CardDeckService;
import org.springframework.samples.dwarf.location.Location;
import org.springframework.samples.dwarf.location.LocationService;
import org.springframework.samples.dwarf.mainboard.MainBoard;
import org.springframework.samples.dwarf.mainboard.MainBoardRepository;
import org.springframework.samples.dwarf.mainboard.MainBoardService;
import org.springframework.samples.dwarf.specialCardDeck.SpecialCardDeckService;

public class MainBoardServiceTest {

    @Mock
    private MainBoardRepository mainBoardRepository;

    @Mock
    private CardDeckService cardDeckService;

    @Mock
    private SpecialCardDeckService specialCardDeckService;

    @Mock
    private SpecialCardService specialCardService;

    @Mock
    private CardService cardService;

    @InjectMocks
    private MainBoardService mainBoardService;

    @Mock
    private LocationService locationService;

    @Mock
    private SpecialCardRepository specialCardRepo;

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
    public void testInitialize_Positive() {
        // Mock behavior of dependencies
        CardDeck mockedCardDeck = new CardDeck();
        when(cardDeckService.initialiate()).thenReturn(mockedCardDeck);

        List<Location> mockedLocations = new ArrayList<>();
        when(locationService.initialize()).thenReturn(mockedLocations);

        MainBoard mockedMainBoard = new MainBoard();
        when(mainBoardRepository.save(any(MainBoard.class))).thenReturn(mockedMainBoard);

        // Call the method
        MainBoard result = mainBoardService.initialize();

        // Verify interactions
        verify(cardDeckService, times(1)).initialiate();
        verify(locationService, times(1)).initialize();
        verify(mainBoardRepository, times(1)).save(any(MainBoard.class));

        // Assert the result
        assertNotNull(result);
    }

    // @Test
    // public void testHoldACouncilAction_Positive() {

    // // Initialize mocks
    // Card c1 = new Card();
    // Card c2 = new Card();
    // c1.setDescription("abc");
    // c2.setDescription("abcd");
    // c1.setPosition(0);
    // c2.setPosition(1);

    // Location l1 = new Location();
    // Location l2 = new Location();
    // l1.setPosition(0);
    // l2.setPosition(1);
    // l1.setCards(Arrays.asList(c1));
    // l1.setCards(Arrays.asList(c2));

    // List<Location> mockedLocations = Arrays.asList(l1, l2);

    // CardDeck mockedCardDeck = new CardDeck();
    // mockedCardDeck.setCards(Arrays.asList(c1, c2));

    // MainBoard mockedMainBoard = mainBoardService.initialize();

    // List<Card> mockedRemovedCards = Arrays.asList(new Card(), new Card());

    // when(cardDeckService.shuffleAndSaveCards(any(CardDeck.class),
    // anyList())).thenReturn(mockedCardDeck);
    // when(locationService.removeLastCard(any(Location.class))).thenReturn(mockedRemovedCards.get(0),
    // mockedRemovedCards.get(1));

    // // Call the method
    // mainBoardService.holdACouncilAction(mockedMainBoard);

    // // Verify interactions
    // verify(mainBoardRepository, times(1)).findById(any());
    // verify(cardDeckService, times(1)).shuffleAndSaveCards(any(CardDeck.class),
    // anyList());
    // verify(locationService, times(2)).removeLastCard(any(Location.class));

    // // Assert that the state of MainBoard is modified as expected
    // assertEquals(mockedCardDeck, mockedMainBoard.getCardDeck());
    // assertEquals(0, mockedMainBoard.getLocations().get(0).getCards().size());
    // assertEquals(0, mockedMainBoard.getLocations().get(1).getCards().size());
    // }

}
