package org.springframework.samples.dwarf.MainBoard;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.util.Pair;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.CardService;
import org.springframework.samples.dwarf.card.CardType;
import org.springframework.samples.dwarf.card.SpecialCard;
import org.springframework.samples.dwarf.card.SpecialCardRepository;
import org.springframework.samples.dwarf.cardDeck.CardDeck;
import org.springframework.samples.dwarf.cardDeck.CardDeckService;
import org.springframework.samples.dwarf.dwarf.Dwarf;
import org.springframework.samples.dwarf.dwarf.DwarfRepository;
import org.springframework.samples.dwarf.game.Game;
import org.springframework.samples.dwarf.game.GameService;
import org.springframework.samples.dwarf.location.Location;
import org.springframework.samples.dwarf.location.LocationService;
import org.springframework.samples.dwarf.mainboard.MainBoard;
import org.springframework.samples.dwarf.mainboard.MainBoardRepository;
import org.springframework.samples.dwarf.mainboard.MainBoardService;
import org.springframework.samples.dwarf.player.Player;

public class MainBoardServiceTest {

    @Mock
    private MainBoardRepository mainBoardRepository;

    @Mock
    private CardDeckService cardDeckService;

    @Mock
    private CardService cardService;

    @Mock
    private LocationService locService;

    @Mock
    private SpecialCardRepository specCardRepo;

    @InjectMocks
    private MainBoardService mainBoardService;

    @Mock
    private LocationService locationService;

    @Mock
    private SpecialCardRepository specialCardRepo;

    @Mock
    private DwarfRepository dwarfRepository;

    @Mock
    private GameService gameService;

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
    public void testRunAmokAction() {
        Location l = new Location();
        List<Location> locations = List.of(l, l);

        MainBoard mb = new MainBoard();
        mb.setLocations(locations);

        when(locService.shuffleLocation(any(Location.class))).thenReturn(l);

        List<Location> lc = mainBoardService.runAmokAction(mb);

        assertEquals(2, lc.size());
        assertEquals(l, lc.get(0));
    }

    @Test
    void testGetChoosedCards() {
        Dwarf d1 = new Dwarf();
        Card c1 = new Card();
        Player p1 = new Player();
        c1.setName("Test1");
        p1.setName("Test1");
        d1.setCard(c1);
        d1.setPlayer(p1);

        Dwarf d2 = new Dwarf();
        Card c2 = new Card();
        Player p2 = new Player();
        c2.setName("Test2");
        p2.setName("Test2");
        d2.setCard(c2);
        d2.setPlayer(p2);

        ArrayList<Pair<Player, Card>> res = mainBoardService.getChoosedCards(List.of(d1, d2));

        assertEquals(2, res.size());
        assertEquals(p1, res.get(0).getFirst());
        assertEquals(p2, res.get(1).getFirst());
        assertEquals(c1, res.get(0).getSecond());
        assertEquals(c2, res.get(1).getSecond());
    }

    @Test
    void testSplitCardsByType() {
        CardType ct1 = new CardType();
        ct1.setName("Test1");
        CardType ct2 = new CardType();
        ct2.setName("Test2");

        Player p1 = new Player();
        p1.setName("Test1");
        Player p2 = new Player();
        p2.setName("Test2");

        Card c1 = new Card();
        c1.setName("Test1");
        c1.setCardType(ct1);

        Card c3 = new Card();
        c3.setName("Test3");
        c3.setCardType(ct1);

        Card c2 = new Card();
        c2.setName("Test2");
        c2.setCardType(ct2);

        Card c4 = new Card();
        c4.setName("Test4");
        c4.setCardType(ct2);

        ArrayList<Pair<Player, Card>> cards = new ArrayList<>();
        cards.add(Pair.of(p1, c1));
        cards.add(Pair.of(p2, c2));
        cards.add(Pair.of(p1, c3));
        cards.add(Pair.of(p2, c4));

        HashMap<String, ArrayList<Pair<Player, Card>>> res = mainBoardService.splitCardsByType(cards);

        assertEquals(true, res.containsKey(ct1.getName()));
        assertEquals(true, res.containsKey(ct2.getName()));

        ArrayList<Pair<Player, Card>> t1 = res.get(ct1.getName());
        ArrayList<Pair<Player, Card>> t2 = res.get(ct2.getName());

        assertEquals(2, t1.size());
        assertEquals(2, t2.size());
        assertEquals(Pair.of(p1, c1), t1.get(0));
        assertEquals(Pair.of(p1, c3), t1.get(1));
        assertEquals(Pair.of(p2, c2), t2.get(0));
        assertEquals(Pair.of(p2, c4), t2.get(1));

    }

    @Test
    void testFaseOrcosNoOrcCardsCurrently() {
        Game g = new Game();
        MainBoard mb = new MainBoard();

        CardType ct1 = new CardType();
        ct1.setName("Test1");
        Card c1 = new Card();
        c1.setName("Test1");
        c1.setCardType(ct1);
        Card c2 = new Card();
        c2.setName("Test2");
        c2.setCardType(ct1);

        Location l1 = new Location();
        l1.setCards(List.of(c1));
        Location l2 = new Location();
        l2.setCards(List.of(c2));
        mb.setLocations(List.of(l1, l2));

        g.setMainBoard(mb);

        Boolean res = mainBoardService.faseOrcos(g, null);
        assertTrue(res);
    }

    @Test
    void testFaseOrcosDefended() {
        Game g = new Game();
        MainBoard mb = new MainBoard();
        Player p = new Player();
        p.setName("test");

        CardType ct1 = new CardType();
        ct1.setName("Test1");
        CardType ct2 = new CardType();
        ct2.setName("OrcCard");

        Card c1 = new Card();
        c1.setName("Test1");
        c1.setCardType(ct1);
        Card c2 = new Card();
        c2.setName("Test2");
        c2.setCardType(ct1);

        Card c3 = new Card();
        c3.setName("Orc Raiders");
        c3.setCardType(ct2);

        Location l1 = new Location();
        l1.setCards(List.of(c1));
        Location l2 = new Location();
        l2.setCards(List.of(c2));
        Location l3 = new Location();
        l3.setCards(List.of(c3));
        mb.setLocations(List.of(l1, l2, l3));

        g.setMainBoard(mb);

        ArrayList<Pair<Player, Card>> orcCards = new ArrayList<>();
        orcCards.add(Pair.of(p, c3));

        Boolean res = mainBoardService.faseOrcos(g, orcCards);
        assertTrue(res);
    }

    @Test
    void testFaseOrcosNotDefended() {
        Game g = new Game();
        MainBoard mb = new MainBoard();
        Player p = new Player();
        p.setName("test");

        CardType ct1 = new CardType();
        ct1.setName("Test1");
        CardType ct2 = new CardType();
        ct2.setName("OrcCard");

        Card c1 = new Card();
        c1.setName("Test1");
        c1.setCardType(ct1);
        Card c2 = new Card();
        c2.setName("Test2");
        c2.setCardType(ct1);

        Card c3 = new Card();
        c3.setName("Orc Raiders");
        c3.setCardType(ct2);

        Location l1 = new Location();
        l1.setCards(List.of(c1));
        Location l2 = new Location();
        l2.setCards(List.of(c2));
        Location l3 = new Location();
        l3.setCards(List.of(c3));
        mb.setLocations(List.of(l1, l2, l3));

        g.setMainBoard(mb);

        ArrayList<Pair<Player, Card>> orcCards = new ArrayList<>();

        Boolean res = mainBoardService.faseOrcos(g, orcCards);
        assertTrue(!res);
    }

    @Test
    void testApplySingleCardWhenSpecialCardChangeDwarf() {
        Dwarf d1 = new Dwarf();
        Card c1 = new Card();
        Card c2 = new Card();
        Player p1 = new Player();

        CardType ct2 = new CardType();
        ct2.setName("OrcCard");

        Integer position = 1;
        c1.setName("Test1");
        c1.setPosition(position);
        c1.setCardType(ct2);

        c2.setName("Test2");
        c2.setPosition(position);

        p1.setName("Test1");
        d1.setCard(c1);
        d1.setPlayer(p1);

        List<Dwarf> roundDwarves = List.of(d1);
        mainBoardService.applySingleCardWhenSpecialCard(roundDwarves, c2);

        assertTrue(!d1.getNeedsToBeResolved());

        verify(cardService).adwardMedalSingleAction(p1, c1);
    }

    @Test
    void testFaseResolucionAcciones() {
        // Configurar el estado del juego para el test
        CardType ct1 = new CardType();
        CardType ct2 = new CardType();
        ct1.setName("Other");
        ct2.setName("HelpCard");

        Card c1 = new Card();
        Card c2 = new Card();
        c1.setName("Test1");
        c1.setPosition(1);
        c1.setCardType(ct1);
        c2.setName("Test2");
        c2.setPosition(2);
        c2.setCardType(ct2);

        Player p1 = new Player();
        Player p2 = new Player();
        p1.setName("Player1");
        p2.setName("Player2");

        Dwarf d1 = new Dwarf();
        Dwarf d2 = new Dwarf();
        d1.setCard(c1);
        d1.setPlayer(p1);
        d1.setRound(1);
        d2.setCard(c2);
        d2.setPlayer(p2);
        d2.setRound(1);

        List<Dwarf> dwarves = new ArrayList<>();
        dwarves.add(d1);
        dwarves.add(d2);

        Location l1 = new Location();
        Location l2 = new Location();
        l1.setCards(List.of(c1));
        l2.setCards(List.of(c2));

        MainBoard mb = new MainBoard();
        mb.setLocations(List.of(l1, l2));
        mb.setCardDeck(null);

        Game game = new Game();
        game.setMainBoard(mb);
        game.setPlayerCreator(p1);
        game.setPlayers(Arrays.asList(p1, p2));
        game.setDwarves(dwarves);
        game.setRound(1);

        ArrayList<Pair<Player, Card>> cards = new ArrayList<>();
        cards.add(Pair.of(p1, c1));
        cards.add(Pair.of(p2, c2));

        when(dwarfRepository.findAll()).thenReturn(dwarves);

        // Llamada al método que estamos probando
        ArrayList<Pair<Player, Card>> result = mainBoardService.faseResolucionAcciones(game);

        // Verificar resultados y comportamientos
        assertNotNull(result);
    }

    @Test
    void testFaseResolucionAcciones_NoHelpCards() {
        // Configurar el estado del juego para el test
        CardType ct1 = new CardType();
        ct1.setName("Other");

        Card c1 = new Card();
        c1.setName("Test1");
        c1.setPosition(1);
        c1.setCardType(ct1);

        Player p1 = new Player();
        p1.setName("Player1");

        Dwarf d1 = new Dwarf();
        d1.setCard(c1);
        d1.setPlayer(p1);
        d1.setRound(1);

        List<Dwarf> dwarves = new ArrayList<>();
        dwarves.add(d1);

        Location l1 = new Location();
        l1.setCards(List.of(c1));

        MainBoard mb = new MainBoard();
        mb.setLocations(List.of(l1));
        mb.setCardDeck(null);

        Game game = new Game();
        game.setMainBoard(mb);
        game.setPlayerCreator(p1);
        game.setPlayers(Collections.singletonList(p1));
        game.setDwarves(dwarves);
        game.setRound(1);

        when(dwarfRepository.findAll()).thenReturn(dwarves);

        // Llamada al método que estamos probando
        ArrayList<Pair<Player, Card>> result = mainBoardService.faseResolucionAcciones(game);

        // Verificar resultados y comportamientos
        assertNull(result);
    }

}
