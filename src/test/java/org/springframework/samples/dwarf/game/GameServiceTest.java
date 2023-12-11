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
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.util.Pair;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.CardType;
import org.springframework.samples.dwarf.cardDeck.CardDeckService;
import org.springframework.samples.dwarf.dwarf.Dwarf;
import org.springframework.samples.dwarf.game.Game;
import org.springframework.samples.dwarf.game.GameRepository;
import org.springframework.samples.dwarf.game.GameService;
import org.springframework.samples.dwarf.mainboard.MainBoard;
import org.springframework.samples.dwarf.mainboard.MainBoardService;
import org.springframework.samples.dwarf.player.Player;
import org.springframework.samples.dwarf.player.PlayerRepository;
import org.springframework.samples.dwarf.user.UserRepository;
import org.springframework.samples.dwarf.user.UserService;

public class GameServiceTest {

    private GameService gameService;
    private GameRepository gameRepository;
    private PlayerRepository playerRepository;
    private UserService userService;
    private MainBoardService mbs;
    private CardDeckService cds;

    @BeforeEach
    public void setUp() {
        gameRepository = mock(GameRepository.class);
        playerRepository = mock(PlayerRepository.class);
        mbs = mock(MainBoardService.class);
        cds = mock(CardDeckService.class);
        gameService = new GameService(gameRepository, playerRepository, userService, mbs, cds, null);
    }

    @Test
    public void testGetAllGames() {
        List<Game> mockGames = Arrays.asList(new Game(), new Game());
        when(gameRepository.findAll()).thenReturn(mockGames);

        List<Game> result = gameService.getAllGames();
        assertEquals(2, result.size(), "Número incorrecto de juegos recuperados");
    }

    @Test
    public void testDelete() {
        Game entityToDelete = new Game();
        when(gameService.getGameById(1)).thenReturn(Optional.of(entityToDelete));

        gameService.delete(1);
        verify(gameRepository).deleteById(1);
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
    public void testOrcCardKnockersAction() {
        // Datos de prueba
        Player player1 = new Player();
        player1.setIron(5);

        Player player2 = new Player();
        player2.setIron(0);

        List<Player> players = Arrays.asList(player1, player2);

        // Configuración del repositorio mock
        when(playerRepository.findAll()).thenReturn(players);

        // Llamada al método que queremos probar
        gameService.orcCardKnockersAction(mock(Game.class));

        // Verificaciones
        assertEquals(4, player1.getIron()); // 5 - 1 = 4
        assertEquals(0, player2.getIron()); // La cantidad de hierro no debería ser menor que 0
    }

        @Test
    public void testOrcCardSidheAction() {
        // Datos de prueba
        Player player1 = new Player();
        player1.setIron(5);
        player1.setGold(10);

        Player player2 = new Player();
        player2.setIron(0);
        player2.setGold(0);

        Player player3 = new Player();
        player3.setIron(0);
        player3.setGold(1);

        List<Player> players = Arrays.asList(player1, player2, player3);

        // Configuración del repositorio mock
        when(playerRepository.findAll()).thenReturn(players);

        // Llamada al método que queremos probar
        gameService.orcCardSidheAction(mock(Game.class));

        // Verificaciones
        assertEquals(7, player1.getIron()); // 5 + 2 = 7
        assertEquals(8, player1.getGold()); // 10 -2 = 8

        assertEquals(0, player2.getIron()); //No tenia oro por lo que no le dan hierro
        assertEquals(0, player2.getGold());

        assertEquals(1, player3.getIron()); //Solo tenia uno de oro por lo que solo le da uno de hierro
        assertEquals(0, player3.getGold());
    }

    @Test
    public void testOrcCardDragonAction() {
        // Datos de prueba
        Player player1 = new Player();
        player1.setGold(5);

        Player player2 = new Player();
        player2.setGold(0);

        List<Player> players = Arrays.asList(player1, player2);

        // Configuración del repositorio mock
        when(playerRepository.findAll()).thenReturn(players);

        // Llamada al método que queremos probar
        gameService.orcCardDragonAction(mock(Game.class));

        // Verificaciones
        assertEquals(4, player1.getGold()); // 5 - 1 = 4
        assertEquals(0, player2.getGold()); // La cantidad de oro no debería ser menor que 0
    }

     @Test
    public void testOrcCardGreatDragonAction() {
        // Datos de prueba
        Player player1 = new Player();
        player1.setGold(5);

        Player player2 = new Player();
        player2.setGold(0);

        List<Player> players = Arrays.asList(player1, player2);

        // Configuración del repositorio mock
        when(playerRepository.findAll()).thenReturn(players);

        // Llamada al método que queremos probar
        gameService.orcCardGreatDragonAction(mock(Game.class));

        // Verificaciones
        assertEquals(0, player1.getGold()); 
        assertEquals(0, player2.getGold()); 
    }

    @Test
    void testChangePlayerStart() {
        // Arrange
        Game game = new Game();
        MainBoard mainBoard = new MainBoard();
        game.setMainBoard(mainBoard);
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();
        player1.setName("Player1");
        player2.setName("Player2");
        player3.setName("Player3");
        player4.setName("Player4");
        player1.setId(1);
        player2.setId(2);
        player3.setId(3);
        player4.setId(4);

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

        List <Card> l = new ArrayList<>();
        l.add(helpCard1);
        l.add(helpCard2);
        l.add(helpCard3);
        l.add(helpCard4);
        //mainBoard.setCards(l);
        
        helpCards.add(Pair.of(player1, helpCard1));
        helpCards.add(Pair.of(player2, helpCard2));
        helpCards.add(Pair.of(player3, helpCard3));
        helpCards.add(Pair.of(player4, helpCard4));


        game.setPlayerStart(player3);

        List<Player> players = Arrays.asList(player1, player2, player3, player4);
        when(playerRepository.findAll()).thenReturn(players);
        

        // Act
        gameService.changePlayerStart(game, helpCards);
        
        assertEquals(player4, game.getPlayerStart());
    }    
    
   
}
