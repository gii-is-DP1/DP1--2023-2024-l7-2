package org.springframework.samples.dwarf.game;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.samples.dwarf.object.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.util.Pair;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.CardService;
import org.springframework.samples.dwarf.card.CardType;
import org.springframework.samples.dwarf.card.SpecialCardService;
import org.springframework.samples.dwarf.cardDeck.CardDeck;
import org.springframework.samples.dwarf.cardDeck.CardDeckService;
import org.springframework.samples.dwarf.chat.ChatService;
import org.springframework.samples.dwarf.dwarf.Dwarf;
import org.springframework.samples.dwarf.dwarf.DwarfService;
import org.springframework.samples.dwarf.game.Game;
import org.springframework.samples.dwarf.game.GameRepository;
import org.springframework.samples.dwarf.game.GameService;
import org.springframework.samples.dwarf.location.Location;
import org.springframework.samples.dwarf.location.LocationService;
import org.springframework.samples.dwarf.mainboard.MainBoard;
import org.springframework.samples.dwarf.mainboard.MainBoardService;
import org.springframework.samples.dwarf.player.Player;
import org.springframework.samples.dwarf.player.PlayerRepository;
import org.springframework.samples.dwarf.player.PlayerService;
import org.springframework.samples.dwarf.spectator.Spectator;
import org.springframework.samples.dwarf.user.User;
import org.springframework.samples.dwarf.user.UserRepository;
import org.springframework.samples.dwarf.user.UserService;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
public class GameServiceTest {

    @Mock
    private GameService gameService;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserService userService;

    @Mock
    private MainBoardService mbs;

    @Mock
    private CardDeckService cds;

    @Mock
    private PlayerService ps;

    @Mock
    private LocationService ls;

    @Mock
    private CardService cs;

    @Mock
    private ChatService chatservice;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private SpecialCardService scs;

    @Mock
    private DwarfService ds;

    private GameService GameService;

    @Autowired
    private GameService realGameService;

    @BeforeEach
    public void setUp() {
        // gameRepository = mock(GameRepository.class);
        // mbs = mock(MainBoardService.class);
        // cds = mock(CardDeckService.class);
        gameService = new GameService(gameRepository, ps, userService, mbs, cds, ls,scs,ds,null);
    }

    @Test
    public void testGetAllGames() {
        List<Game> mockGames = Arrays.asList(new Game(), new Game());
        when(gameRepository.findAll()).thenReturn(mockGames);

        List<Game> result = gameService.getAllGames();
        assertEquals(2, result.size(), "Número incorrecto de juegos recuperados");
    }

    @Test
    public void testGetWaitingGames() {
    
        Game game1 = new Game();
        game1.setId(1);
        game1.setStart(null);

        Game game2 = new Game();
        game2.setId(2);
        game2.setStart(LocalDateTime.now().minusMinutes(30));  


    
        when(gameRepository.findByStart(null)).thenReturn(Arrays.asList(game1));

   
        List<Game> waitingGames = gameService.getWaitingGames();
        assertEquals(1, waitingGames.size(), "Number of waiting games should be 1");
        assertTrue(waitingGames.contains(game1), "The waiting games list should contain game1");
        assertFalse(waitingGames.contains(game2), "The waiting games list should not contain game2");
    }

    @Test
    public void testGameContainsSpectator() {
        
        User user1 = new User();
        user1.setId(1);

        User user2 = new User();
        user2.setId(2);

        Spectator spectator1 = new Spectator();
        spectator1.setId(1);
        spectator1.setUser(user1);

        Spectator spectator2 = new Spectator();
        spectator2.setId(2);
        spectator2.setUser(user2);

        Game game = new Game();
        List<Spectator> sp = new ArrayList<>();
        game.setSpectators(sp);
        game.getSpectators().addAll(Arrays.asList(spectator1));

        
        boolean containsUser1 = gameService.gameContainsSpectator(game, user1);
        boolean containsUser2 = gameService.gameContainsSpectator(game, user2);

    
        assertTrue(containsUser1, "The game should contain spectator1");
        assertFalse(containsUser2, "The game should not contain spectator2");
    }

    @Test
    public void testDelete() {
        
        Integer gameId = 1;
        Game gameToDelete = new Game();
        gameToDelete.setId(gameId);

        
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(gameToDelete));

       
        gameService.delete(gameId);

        
        verify(gameRepository).delete(gameToDelete);
    }

    @Test
    public void testDeleteGameNotFound() {
        
        Integer gameId = 1;

        
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        
        assertThrows(NoSuchElementException.class, () -> gameService.delete(gameId));
    }


    @Test
    public void testGetGameByCode() {
        String gameCode = "TEST123";
        Game mockGame = new Game();
        when(gameRepository.findByCode(gameCode)).thenReturn(Collections.singletonList(mockGame));

        Game result = gameService.getGameByCode(gameCode);
        assertNotNull(result, "Juego no encontrado por código");
    }

    @Test
    public void testGetFinishedGames() {
        List<Game> mockFinishedGames = Arrays.asList(new Game(), new Game());
        when(gameRepository.findByFinishIsNotNull()).thenReturn(mockFinishedGames);
        List<Game> result = gameService.getFinishedGames();
        assertEquals(2, result.size(), "Número incorrecto de juegos finalizados recuperados");
    }

    @Test
    public void testGetGamesLike() {
        // Arrange
        String pattern = "Test";
        Game game1 = new Game();
        game1.setName("Test Game 1");

        Game game2 = new Game();
        game2.setName("Test Game 2");

        Game game3 = new Game();
        game3.setName("Test Game 3");

       

        when(gameRepository.findByNameContains(pattern)).thenReturn(Arrays.asList(game1, game3));

        
        List<Game> result = gameService.getGamesLike(pattern);

        
        assertEquals(2, result.size(), "Number of games retrieved should be 2");
        assertTrue(result.contains(game1), "The result should contain game1");
        assertFalse(result.contains(game2), "The result should not contain game2");
        assertTrue(result.contains(game3), "The result should contain game3");
    }

    @Test
    public void testGetOngoingGames() {
        // Arrange
        Game ongoingGame1 = new Game();
        ongoingGame1.setStart(LocalDateTime.now().minusHours(1));
        ongoingGame1.setFinish(null);

        Game ongoingGame2 = new Game();
        ongoingGame2.setStart(LocalDateTime.now().minusMinutes(30));
        ongoingGame2.setFinish(null);

        Game finishedGame = new Game();
        finishedGame.setStart(LocalDateTime.now().minusDays(1));
        finishedGame.setFinish(LocalDateTime.now().minusHours(23));


        when(gameRepository.findByFinishIsNullAndStartIsNotNull()).thenReturn(Arrays.asList(ongoingGame1, ongoingGame2));

        
        List<Game> result = gameService.getOngoingGames();

        
        assertEquals(2, result.size(), "Number of ongoing games should be 2");
        assertTrue(result.contains(ongoingGame1), "The result should contain ongoingGame1");
        assertTrue(result.contains(ongoingGame2), "The result should contain ongoingGame2");
        assertFalse(result.contains(finishedGame), "The result should not contain finishedGame");
    }


    @Test
    public void testGetAllPublicGames() {
        // Arrange
        Game publicGame1 = new Game();
        publicGame1.setIsPublic(true);

        Game publicGame2 = new Game();
        publicGame2.setIsPublic(true);

        Game privateGame = new Game();
        privateGame.setIsPublic(false);

       

        // Stubbing the behavior of the gameRepository.findAllPublicGames method
        when(gameRepository.findAllPublicGames()).thenReturn(Arrays.asList(publicGame1, publicGame2));

        // Act
        List<Game> result = gameService.getAllPublicGames();

        // Assert
        assertEquals(2, result.size(), "Number of public games should be 2");
        assertTrue(result.contains(publicGame1), "The result should contain publicGame1");
        assertTrue(result.contains(publicGame2), "The result should contain publicGame2");
        assertFalse(result.contains(privateGame), "The result should not contain privateGame");
    }


    @Test
    public void testGetAllPublicGamesPagination() {
        // Arrange
        Pageable pageable = mock(Pageable.class);
        Game publicGame1 = new Game();
        publicGame1.setIsPublic(true);

        Game publicGame2 = new Game();
        publicGame2.setIsPublic(true);

        Game privateGame = new Game();
        privateGame.setIsPublic(false);

        Page<Game> publicGamesPage = new PageImpl<>(Arrays.asList(publicGame1, publicGame2));

        // Stubbing the behavior of the gameRepository.findAllPublicGames method with pagination
        when(gameRepository.findAllPublicGames(pageable)).thenReturn(publicGamesPage);

        // Act
        Page<Game> result = gameService.getAllPublicGames(pageable);

        // Assert
        assertEquals(2, result.getContent().size(), "Number of public games should be 2");
        assertTrue(result.getContent().contains(publicGame1), "The result should contain publicGame1");
        assertTrue(result.getContent().contains(publicGame2), "The result should contain publicGame2");
        assertFalse(result.getContent().contains(privateGame), "The result should not contain privateGame");
    }

    @Test
    public void testGetRoundDwarfs() {
        // Arrange
        Game game = new Game();
        Integer round = 1;

        Dwarf dwarf1 = new Dwarf();
        dwarf1.setRound(1);
        dwarf1.setPlayer(new Player());
        dwarf1.setCard(new Card());

        Dwarf dwarf2 = new Dwarf();
        dwarf2.setRound(1);
        dwarf2.setPlayer(new Player());
        dwarf2.setCard(new Card());

        Dwarf dwarf3 = new Dwarf();
        dwarf3.setRound(2);  // Different round
        dwarf3.setPlayer(new Player());
        dwarf3.setCard(new Card());

        game.setDwarves(Arrays.asList(dwarf1, dwarf2, dwarf3));

        // Act
        List<Dwarf> result = gameService.getRoundDwarfs(game, round);

        // Assert
        assertEquals(2, result.size(), "Number of dwarfs for the given round should be 2");
        assertTrue(result.contains(dwarf1), "The result should contain dwarf1");
        assertTrue(result.contains(dwarf2), "The result should contain dwarf2");
        assertFalse(result.contains(dwarf3), "The result should not contain dwarf3");
    }

    @Test
    public void testGameContainsPlayer() {
        // Arrange
        User userToCheck = new User();
        Player player1 = new Player();
        player1.setUser(userToCheck);

        Player player2 = new Player();
        player2.setUser(new User());  // Different user

        Game game = new Game();
        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        game.setPlayers(players);

        // Act
        boolean result = gameService.gameContainsPlayer(game, userToCheck);

        // Assert
        assertTrue(result, "The game should contain the player with the specified user");
    }

    @Test
    public void testGameDoesNotContainPlayer() {
        // Arrange
        User userToCheck = new User();
        Player player = new Player();
        player.setUser(new User());  // Different user

        Game game = new Game();
        game.setPlayers(List.of(player));

        // Act
        boolean result = gameService.gameContainsPlayer(game, userToCheck);

        // Assert
        assertFalse(result, "The game should not contain the player with the specified user");
    }

    @Test
    public void testGameDoesNotContainSpectator() {
        // Arrange
        User userToCheck = new User();
        Spectator spectator = new Spectator();
        spectator.setUser(new User());  // Different user

        Game game = new Game();
        game.setSpectators(List.of(spectator));

        // Act
        boolean result = gameService.gameContainsSpectator(game, userToCheck);

        // Assert
        assertFalse(result, "The game should not contain the spectator with the specified user");
    }

 @Test
    void testChangePlayerStart() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Arrange
        Game game = new Game();
        MainBoard mainBoard = new MainBoard();
        game.setMainBoard(mainBoard);
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();

        player1.setName("Player1");
        player2.setName("Player2");
        player3.setName("Player3");

        player1.setId(1);
        player2.setId(2);
        player3.setId(3);

        Location location = new Location();
        location.setPosition(1);
        List<Location> locat = new ArrayList<>();
        locat.add(location);
        mainBoard.setLocations(locat);

        game.setPlayerCreator(player1);
        ArrayList<Pair<Player, Card>> helpCards = new ArrayList<>();

        Card helpCard1 = new Card();
        CardType cardType1 = new CardType();
        cardType1.setName("HelpCard");
        helpCard1.setCardType(cardType1);

        Card helpCard2 = new Card();
        helpCard2.setCardType(cardType1);
        Card helpCard3 = new Card();
        helpCard3.setCardType(cardType1);
        Card helpCard4 = new Card();
        helpCard4.setCardType(cardType1);

        List<Card> l = new ArrayList<>();
        l.add(helpCard1);
        l.add(helpCard2);
        l.add(helpCard3);
        l.add(helpCard4);
        location.setCards(l);

        helpCards.add(Pair.of(player1, helpCard1));
        helpCards.add(Pair.of(player2, helpCard2));
        helpCards.add(Pair.of(player3, helpCard3));

        game.setPlayerStart(player2);

        List<Player> players = Arrays.asList(player1, player2, player3);
        when(playerRepository.findAll()).thenReturn(players);
        game.setPlayers(players);

        // Act
        gameService.changePlayerStart(game, helpCards);

        // Assert
        assertEquals(player3, game.getPlayerStart());
    }

    @Test
    public void testResign() {
        Game game = new Game();
        Player player = new Player();
        List<Player> players = new ArrayList<>();
        game.setPlayers(players);
    
        game.getPlayers().add(player);

        gameService.resign(game, player);

        assertEquals(0, player.getGold(), "Gold should be reset to 0");
        assertEquals(0, player.getIron(), "Iron should be reset to 0");
        assertNull(player.getMedal(), "Medal should be reset to null");
        assertNull(player.getObjects(), "Objects should be reset to null");
        assertEquals(0, player.getSteal(), "Steal should be reset to 0");
    }
    
   

    
   @Test
   void testTurnWhenSpecialCard(){

    Game g = realGameService.getGameByCode("test-code");
    List<Player> players = g.getPlayers();
    Player starter = g.getPlayerStart();


    List<Dwarf> dwarves = g.getDwarves();
    Integer round = g.getRound();
    List<Dwarf> thisRoundDwarves = dwarves.stream().filter(d -> d.getRound() == round
            && d.getPlayer() != null).toList();

    List<Player> returnedPlayers = realGameService.getRemainingTurns(players,thisRoundDwarves,starter);

    System.out.println(returnedPlayers);

   }


   @Test
   void getGameWinnerTest1() {
    Player player1 = new Player();
    Player player2 = new Player();
    Player player3 = new Player();

    player1.setName("Player1");
    player2.setName("Player2");
    player3.setName("Player3");

    player1.setId(1);
    player2.setId(2);
    player3.setId(3);
    
    player1.setSteal(99);
    player1.setGold(0);
    player1.setIron(0);
    player1.setObjects(List.of());

    player2.setSteal(0);
    player2.setGold(0);
    player2.setIron(50);
    player2.setObjects(List.of());


    player3.setSteal(0);
    player3.setGold(0);
    player3.setIron(99);
    player3.setObjects(List.of());

    Game g = new Game();
    g.setPlayers(List.of(player1,player2,player3));

    Player winner = gameService.getGameWinner(g);
    
    assertEquals(player1, winner);
   }

   @Test
   void getGameWinnerTest2() {
    Player player1 = new Player();
    Player player2 = new Player();
    Player player3 = new Player();

    player1.setName("Player1");
    player2.setName("Player2");
    player3.setName("Player3");

    player1.setId(1);
    player2.setId(2);
    player3.setId(3);
    
    player1.setSteal(99);
    player1.setGold(50);
    player1.setIron(0);
    player1.setMedal(0);
    player1.setObjects(List.of());

    player2.setSteal(99);
    player2.setGold(50);
    player2.setIron(0);
    player2.setMedal(50);
    player2.setObjects(List.of());


    player3.setSteal(0);
    player3.setGold(0);
    player3.setIron(99);
    player2.setMedal(0);
    player3.setObjects(List.of());

    Game g = new Game();
    g.setPlayers(List.of(player1,player2,player3));

    Player winner = gameService.getGameWinner(g);
    
    assertEquals(player2, winner);
   }

   @Test
   void getGameWinnerTest3() {
    Player player1 = new Player();
    Player player2 = new Player();
    Player player3 = new Player();

    player1.setName("Player1");
    player2.setName("Player2");
    player3.setName("Player3");

    player1.setId(1);
    player2.setId(2);
    player3.setId(3);
    
    player1.setSteal(99);
    player1.setGold(50);
    player1.setIron(0);
    player1.setMedal(50);
    player1.setObjects(List.of());

    player2.setSteal(99);
    player2.setGold(50);
    player2.setIron(99);
    player2.setMedal(50);
    player2.setObjects(List.of());


    player3.setSteal(0);
    player3.setGold(0);
    player3.setIron(99);
    player3.setMedal(0);
    player3.setObjects(List.of());

    Game g = new Game();
    g.setPlayers(List.of(player1,player2,player3));

    Player winner = gameService.getGameWinner(g);
    
    assertEquals(player2, winner);
   }

   @Test
   void getGameWinnerTest4() {
    Player player1 = new Player();
    Player player2 = new Player();
    Player player3 = new Player();

    player1.setName("Player1");
    player2.setName("Player2");
    player3.setName("Player3");

    player1.setId(1);
    player2.setId(2);
    player3.setId(3);
    
    player1.setSteal(99);
    player1.setGold(50);
    player1.setIron(50);
    player1.setMedal(50);
    player1.setObjects(List.of());

    player2.setSteal(99);
    player2.setGold(50);
    player2.setIron(99);
    player2.setMedal(50);
    player2.setObjects(List.of());


    player3.setSteal(0);
    player3.setGold(0);
    player3.setIron(99);
    player3.setMedal(0);
    player3.setObjects(List.of());

    Game g = new Game();
    g.setPlayers(List.of(player1,player2,player3));

    Player winner = gameService.getGameWinner(g);
    
    assertEquals(player2, winner);
   }

   @Test
   void getGameWinnerTest5() {
    Player player1 = new Player();
    Player player2 = new Player();
    Player player3 = new Player();

    player1.setName("Player1");
    player2.setName("Player2");
    player3.setName("Player3");

    player1.setId(1);
    player2.setId(2);
    player3.setId(3);
    
    player1.setSteal(99);
    player1.setGold(50);
    player1.setIron(50);
    player1.setMedal(50);
    player1.setObjects(List.of(new Object()));

    player2.setSteal(99);
    player2.setGold(50);
    player2.setIron(99);
    player2.setMedal(50);
    player2.setObjects(List.of());


    player3.setSteal(0);
    player3.setGold(0);
    player3.setIron(99);
    player3.setMedal(0);
    player3.setObjects(List.of());

    Game g = new Game();
    g.setPlayers(List.of(player1,player2,player3));

    Player winner = gameService.getGameWinner(g);
    
    assertEquals(player1, winner);
   }

   @Test
   void gameNeedsToEndShouldReturnTrueBecauseEmtpyCards() {
    Game g = new Game();

    MainBoard mb = new MainBoard();
    CardDeck cd = new CardDeck();
    cd.setCards(List.of());
    mb.setCardDeck(cd);
    g.setMainBoard(mb);

    Boolean res = gameService.needsToEnd(g);

    assertTrue(res);
   }

   @Test
   void gameNeedsToEndShouldReturnTrueBecausePlayerObjects() {
    Game g = new Game();

    MainBoard mb = new MainBoard();
    CardDeck cd = new CardDeck();
    cd.setCards(List.of(new Card()));
    mb.setCardDeck(cd);
    g.setMainBoard(mb);

    Player p = new Player();
    p.setObjects(List.of(new Object(),new Object(),new Object(),new Object()));

    g.setPlayers(List.of(p));

    Boolean res = gameService.needsToEnd(g);

    assertTrue(res);
   }

   @Test
   void gameNeedsToEndShouldReturnTrueBecauseEndTimeNotNull() {
    Game g = new Game();

    MainBoard mb = new MainBoard();
    CardDeck cd = new CardDeck();
    cd.setCards(List.of(new Card()));
    mb.setCardDeck(cd);
    g.setMainBoard(mb);

    Player p = new Player();
    p.setObjects(List.of(new Object()));

    g.setPlayers(List.of(p));

    g.setFinish(LocalDateTime.now());

    Boolean res = gameService.needsToEnd(g);

    assertTrue(res);
   }

   @Test
   void gameNeedsToEndShouldReturnFalse() {
    Game g = new Game();

    MainBoard mb = new MainBoard();
    CardDeck cd = new CardDeck();
    cd.setCards(List.of(new Card()));
    mb.setCardDeck(cd);
    g.setMainBoard(mb);

    Player p = new Player();
    p.setObjects(List.of(new Object()));

    g.setPlayers(List.of(p));

    Boolean res = gameService.needsToEnd(g);

    assertTrue(!res);
   }
}
