package org.springframework.samples.dwarf.game;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.SpecialCard;
import org.springframework.samples.dwarf.cardDeck.CardDeck;
import org.springframework.samples.dwarf.chat.ChatService;
import org.springframework.samples.dwarf.dwarf.Dwarf;
import org.springframework.samples.dwarf.exceptions.ResourceNotFoundException;
import org.springframework.samples.dwarf.invitation.InvitationService;
import org.springframework.samples.dwarf.location.Location;
import org.springframework.samples.dwarf.location.LocationService;
import org.springframework.samples.dwarf.mainboard.MainBoard;
import org.springframework.samples.dwarf.object.Object;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.dwarf.player.Player;
import org.springframework.samples.dwarf.player.PlayerService;
import org.springframework.samples.dwarf.spectator.SpectatorService;
import org.springframework.samples.dwarf.user.User;
import org.springframework.samples.dwarf.user.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers=GameRestController.class, 
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class))
public class GameRestControllerTest {

    private static final String BASE_URL = "/api/v1/game";

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

    @SuppressWarnings("unused")
    @Autowired
    private GameRestController gameRestController;

    @MockBean
    private GameService gameService;

    @MockBean
    private UserService us;
    
    @MockBean
    private PlayerService ps;
    
    @MockBean
    private SpectatorService specservice;
    
    @MockBean
    private ChatService chatservice;
    
    @MockBean
    private InvitationService is;

    
    //private UserService userService;
    //private PlayerService playerService;
    //private LocationService locationService;

    private Game g1;
    private Game g2;
    
    @BeforeEach
    public void setUp() {
        /* 
        gameService = mock(GameService.class);
        userService = mock(UserService.class);
        playerService = mock(PlayerService.class);
        locationService = mock(LocationService.class);
        invitationService = mock(InvitationService.class);

        gameRestController = new GameRestController(gameService, userService, playerService, null, null,
                locationService, null, null, null, null);
        */

        g1 = new Game();
        g2 = new Game();

        g1.setName("name");
        g1.setCode("code");

        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();
        Player player5 = new Player();
        Player player6 = new Player();

        player1.setName("Player1");
        player2.setName("Player2");
        player3.setName("Player3");
        player4.setName("Player4");
        player5.setName("Player5");
        player6.setName("Player6");

        player1.setId(1);
        player2.setId(2);
        player3.setId(3);
        player1.setId(4);
        player2.setId(5);
        player3.setId(6);

        ArrayList<Player> players = new ArrayList<>();
        players.addAll(List.of(player1,player2,player3));

        ArrayList<Player> players2 = new ArrayList<>();
        players2.addAll(List.of(player4,player5,player6));

        g1.setPlayers(players);
        g1.setPlayerCreator(player1);
        g1.setPlayerStart(player1);

        g2.setPlayers(players2);
        g1.setPlayerCreator(player4);
        g2.setPlayerStart(player4);

        MainBoard mb = new MainBoard();
        CardDeck cd = new CardDeck();
        
        Card c1 = new Card();
        c1.setName("sample");
        c1.setDescription("sample");
        c1.setPosition(1);
        cd.setCards(List.of(c1));
        mb.setCardDeck(cd);
        
        Location l = new Location();
        l.setCards(List.of(c1));

        SpecialCard sc1 = new SpecialCard();
        sc1.setName("sample name");
        sc1.setDescription("sample name");

        mb.setSCards(List.of(sc1));
        mb.setLocations(List.of(l));
        
        g1.setMainBoard(mb);
    }

    @Test
    @WithMockUser(username = "sampleUser", authorities = "USER")
    public void testGetAllGames() throws Exception {
        // Configurar el comportamiento del servicio mock
        
        List<Game> mockGames = List.of(g1,g2);
        reset(gameService);

        when(gameService.getAllGames()).thenReturn(mockGames);


        // Ejecutar el método del controlador y verificar el resultado
        mockMvc.perform(get(BASE_URL)).andExpect(status().isOk());
        verify(gameService).getAllGames();
    }

    @Test
    public void testGetAllGamesWithoutAccount() throws Exception {
        // Configurar el comportamiento del servicio mock
        
        List<Game> mockGames = List.of(g1,g2);
        reset(gameService);

        when(gameService.getAllGames()).thenReturn(mockGames);


        // Ejecutar el método del controlador y verificar el resultado
        mockMvc.perform(get(BASE_URL)).andExpect(status().is(401));
    }

    @Test
    @WithMockUser(username = "sampleUser", authorities = "USER")
    public void testGetGameById() throws Exception {
        // Configurar el comportamiento del servicio mock
        reset(gameService);
        Integer gameId = 1;
        when(gameService.getGameById(gameId)).thenReturn(Optional.of(g1));

        mockMvc.perform(get(BASE_URL+"/"+Integer.toString(gameId))).andExpect(status().isOk());

        verify(gameService).getGameById(gameId);
    }

    @Test
    @WithMockUser(username = "sampleUser", authorities = "USER")
    public void getCardsWithUserInGame() throws Exception {
        reset(gameService);
        String gameCode = "TEST123";

        when(gameService.getGameByCode(gameCode)).thenReturn(g1);
        when(gameService.checkPlayerInGameAndGameExists(g1)).thenReturn(true);

        mockMvc.perform(get(BASE_URL+"/play/"+gameCode+"/cards")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "sampleUser", authorities = "USER")
    public void getCardsWithoutUserInGame() throws Exception {
        reset(gameService);
        String gameCode = "TEST123";

        when(gameService.getGameByCode(gameCode)).thenReturn(g1);
        when(gameService.checkPlayerInGameAndGameExists(g1)).thenReturn(false);

        mockMvc.perform(get(BASE_URL+"/play/"+gameCode+"/cards")).andExpect(status().is(400));
    }

    @Test
    @WithMockUser(username = "sampleUser", authorities = "USER")
    public void getAllCards() throws Exception {
        reset(gameService);
        String gameCode = "TEST123";

        when(gameService.getGameByCode(gameCode)).thenReturn(g1);
        when(gameService.checkPlayerInGameAndGameExists(g1)).thenReturn(true);

        mockMvc.perform(get(BASE_URL+"/play/"+gameCode+"/getAllCards")).andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "sampleUser", authorities = "USER")
    public void getCards() throws Exception {
        reset(gameService);
        String gameCode = "TEST123";

        when(gameService.getGameByCode(gameCode)).thenReturn(g1);
        when(gameService.checkPlayerInGameAndGameExists(g1)).thenReturn(true);

        mockMvc.perform(get(BASE_URL+"/play/"+gameCode+"/getCards")).andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "sampleUser", authorities = "USER")
    public void getSpecialCards() throws Exception {
        reset(gameService);
        String gameCode = "TEST123";

        when(gameService.getGameByCode(gameCode)).thenReturn(g1);
        when(gameService.checkPlayerInGameAndGameExists(g1)).thenReturn(true);

        mockMvc.perform(get(BASE_URL+"/play/"+gameCode+"/getSpecialCards")).andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "sampleUser", authorities = "USER")
    public void testPlayGame() throws Exception {
        reset(gameService);
        // Configurar el comportamiento del servicio mock
        String gameCode = "TEST123";
        Game mockGame = new Game();
        when(gameService.getGameByCode(gameCode)).thenReturn(mockGame);

        // Ejecutar el método del controlador y verificar el resultado
        mockMvc.perform(get(BASE_URL+"/play/"+gameCode)).andExpect(status().isOk());
        verify(gameService).getGameByCode(gameCode);
    }

    @Test
    @WithMockUser(username = "sampleUser", authorities = "USER")
    public void testPlayGameThrowError() throws Exception {
        reset(gameService);
        // Configurar el comportamiento del servicio mock
        String gameCode = "TEST123";
        Game mockGame = new Game();
        when(gameService.getGameByCode(gameCode)).thenReturn(null);

        // Ejecutar el método del controlador y verificar el resultado
        mockMvc.perform(get(BASE_URL+"/play/"+gameCode)).andExpect(status().is(404));
    }

    @Test
    @WithMockUser(username = "sampleUser", authorities = "USER")
    public void testjoinGameTooManyPlayers() throws Exception {
        reset(gameService);
        // Configurar el comportamiento del servicio mock
        String gameCode = "TEST123";

        User u = new User();
        u.setName("sample");

        g1.setPlayerStart(null);

        when(us.findCurrentUser()).thenReturn(u);
        when(gameService.getGameByCode(gameCode)).thenReturn(g1);
        when(gameService.gameContainsPlayer(any(Game.class), any(User.class))).thenReturn(false);
        // Ejecutar el método del controlador y verificar el resultado
        mockMvc.perform(post(BASE_URL+"/join/"+gameCode).with(csrf())).andExpect(status().is(403));
    }

    @Test
    @WithMockUser(username = "sampleUser", authorities = "USER")
    public void testjoinGame() throws Exception {
        reset(gameService);
        // Configurar el comportamiento del servicio mock
        String gameCode = "TEST123";

        User u = new User();
        u.setName("sample");


        g1.getPlayers().remove(0);

        when(us.findCurrentUser()).thenReturn(u);
        when(gameService.getGameByCode(gameCode)).thenReturn(g1);
        when(gameService.gameContainsPlayer(any(Game.class), any(User.class))).thenReturn(false);
        // Ejecutar el método del controlador y verificar el resultado
        mockMvc.perform(post(BASE_URL+"/join/"+gameCode).with(csrf())).andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "sampleUser", authorities = "USER")
    public void testjoinGameNotFound() throws Exception {
        reset(gameService);
        // Configurar el comportamiento del servicio mock
        String gameCode = "TEST123";

        User u = new User();
        u.setName("sample");

        when(us.findCurrentUser()).thenReturn(u);
        when(gameService.getGameByCode(gameCode)).thenReturn(null);
        when(gameService.gameContainsPlayer(any(Game.class), any(User.class))).thenReturn(false);
        // Ejecutar el método del controlador y verificar el resultado
        mockMvc.perform(post(BASE_URL+"/join/"+gameCode).with(csrf())).andExpect(status().is(404));
    }

    @Test
    @WithMockUser(username = "sampleUser", authorities = "USER")
    public void testjoinGameAlreadyStarted() throws Exception {
        reset(gameService);
        // Configurar el comportamiento del servicio mock
        String gameCode = "TEST123";

        User u = new User();
        u.setName("sample");

        g1.setStart(LocalDateTime.now());

        when(us.findCurrentUser()).thenReturn(u);
        when(gameService.getGameByCode(gameCode)).thenReturn(g1);
        when(gameService.gameContainsPlayer(any(Game.class), any(User.class))).thenReturn(false);
        // Ejecutar el método del controlador y verificar el resultado
        mockMvc.perform(post(BASE_URL+"/join/"+gameCode).with(csrf())).andExpect(status().is(400));
    }

    @Test
    @WithMockUser(username = "sampleUser", authorities = "USER")
    public void testjoinGamePlayerInGame() throws Exception {
        reset(gameService);
        // Configurar el comportamiento del servicio mock
        String gameCode = "TEST123";

        User u = new User();
        u.setName("sample");

        when(us.findCurrentUser()).thenReturn(u);
        when(gameService.getGameByCode(gameCode)).thenReturn(g1);
        when(gameService.gameContainsPlayer(any(Game.class), any(User.class))).thenReturn(true);
        // Ejecutar el método del controlador y verificar el resultado
        mockMvc.perform(post(BASE_URL+"/join/"+gameCode).with(csrf())).andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "sampleUser", authorities = "USER")
    public void testjoinPublicGame() throws Exception {
        reset(gameService);
        // Configurar el comportamiento del servicio mock
        Integer gameid = 99;

        User u = new User();
        u.setName("sample");
        
        g1.setIsPublic(true);
        g1.getPlayers().remove(0);

        when(us.findCurrentUser()).thenReturn(u);
        when(gameService.getGameById(gameid)).thenReturn(Optional.of(g1));
        when(gameService.gameContainsPlayer(any(Game.class), any(User.class))).thenReturn(false);
        // Ejecutar el método del controlador y verificar el resultado
        mockMvc.perform(post(BASE_URL+"/joinPublic/"+gameid).with(csrf())).andExpect(status().is(200));
    }   

    @Test
    @WithMockUser(username = "sampleUser", authorities = "USER")
    public void testjoinPublicGameTooManyPlayers() throws Exception {
        reset(gameService);
        // Configurar el comportamiento del servicio mock
        Integer gameid = 99;

        User u = new User();
        u.setName("sample");
        
        g1.setIsPublic(true);

        when(us.findCurrentUser()).thenReturn(u);
        when(gameService.getGameById(gameid)).thenReturn(Optional.of(g1));
        when(gameService.gameContainsPlayer(any(Game.class), any(User.class))).thenReturn(false);
        // Ejecutar el método del controlador y verificar el resultado
        mockMvc.perform(post(BASE_URL+"/joinPublic/"+gameid).with(csrf())).andExpect(status().is(403));
    }

    @Test
    @WithMockUser(username = "sampleUser", authorities = "USER")
    public void testjoinPublicGameAlreadyStarted() throws Exception {
        reset(gameService);
        // Configurar el comportamiento del servicio mock
        Integer gameCode = 222;

        User u = new User();
        u.setName("sample");


        g1.getPlayers().remove(0);
        g1.setIsPublic(true);
        g1.setStart(LocalDateTime.now());

        when(us.findCurrentUser()).thenReturn(u);
        when(gameService.getGameById(gameCode)).thenReturn(Optional.of(g1));
        when(gameService.gameContainsPlayer(any(Game.class), any(User.class))).thenReturn(false);
        // Ejecutar el método del controlador y verificar el resultado
        mockMvc.perform(post(BASE_URL+"/joinPublic/"+gameCode).with(csrf())).andExpect(status().is(400));
    }

    @Test
    @WithMockUser(username = "sampleUser", authorities = "USER")
    public void testjoinPublicGameNotFound() throws Exception {
        reset(gameService);
        // Configurar el comportamiento del servicio mock
        Integer gameCode = 123;

        User u = new User();
        u.setName("sample");

        when(us.findCurrentUser()).thenReturn(u);
        when(gameService.getGameById(gameCode)).thenReturn(Optional.empty());
        when(gameService.gameContainsPlayer(any(Game.class), any(User.class))).thenReturn(false);
        // Ejecutar el método del controlador y verificar el resultado
        mockMvc.perform(post(BASE_URL+"/joinPublic/"+gameCode).with(csrf())).andExpect(status().is(500));
    }

    @Test
    @WithMockUser(username = "sampleUser", authorities = "USER")
    public void testjoinPublicGameNotPublic() throws Exception {
        reset(gameService);
        // Configurar el comportamiento del servicio mock
        String gameCode = "TEST123";

        User u = new User();
        u.setName("sample");

        g1.setIsPublic(false);

        when(us.findCurrentUser()).thenReturn(u);
        when(gameService.getGameByCode(gameCode)).thenReturn(g1);
        when(gameService.gameContainsPlayer(any(Game.class), any(User.class))).thenReturn(false);
        // Ejecutar el método del controlador y verificar el resultado
        mockMvc.perform(post(BASE_URL+"/join/"+gameCode).with(csrf())).andExpect(status().is(403));
    }

    @Test
    @WithMockUser(username = "sampleUser", authorities = "USER")
    public void testjoinPublicGamePlayerInGame() throws Exception {
        reset(gameService);
        // Configurar el comportamiento del servicio mock
        Integer gameCode = 23;

        User u = new User();
        u.setName("sample");

        when(us.findCurrentUser()).thenReturn(u);
        when(gameService.getGameById(gameCode)).thenReturn(Optional.of(g1));
        when(gameService.gameContainsPlayer(any(Game.class), any(User.class))).thenReturn(true);
        // Ejecutar el método del controlador y verificar el resultado
        mockMvc.perform(post(BASE_URL+"/joinPublic/"+gameCode).with(csrf())).andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "sampleUser", authorities = "USER")
    public void testCreateGame() throws Exception {
        reset(gameService);
        // Configurar el comportamiento del servicio mock
        Game g = new Game();
        g.setName("test");
        g.setCode("test");

        User u = new User();
        u.setName("sample");

        when(us.findCurrentUser()).thenReturn(u);
        when(gameService.initalize(any(Game.class),any(User.class))).thenReturn(g);

        mockMvc.perform(post(BASE_URL).with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(g))
        ).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "sampleUser", authorities = "USER")
    public void testGetDwarvesByRound() throws Exception {
        reset(gameService);
        // Configurar el comportamiento del servicio mock
        String code="testtest";

        User u = new User();
        u.setName("sample");
        g1.setDwarves(new ArrayList<>());

        when(us.findCurrentUser()).thenReturn(u);
        when(gameService.getGameByCode(code)).thenReturn(g1);
        when(gameService.checkPlayerInGameAndGameExists(g1)).thenReturn(true);

        mockMvc.perform(get(BASE_URL+"/play/"+code+"/dwarves").with(csrf())).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "sampleUser", authorities = "USER")
    public void testGetTurnRoundChange() throws Exception {
        reset(gameService);
        // Configurar el comportamiento del servicio mock
        String code="testtest";

        User u = new User();
        u.setName("sample");
        List<Dwarf> dwarves = new ArrayList<>();
        g1.setDwarves(dwarves);

        when(us.findCurrentUser()).thenReturn(u);
        when(gameService.checkRoundNeedsChange(any(), any())).thenReturn(true);
        when(gameService.getGameByCode(code)).thenReturn(g1);
        when(gameService.checkPlayerInGameAndGameExists(g1)).thenReturn(true);

        mockMvc.perform(get(BASE_URL+"/play/"+code+"/isMyTurn").with(csrf())).andExpect(status().isOk());
        verify(gameService).handleRoundChange(any(Game.class));
    }

    @Test
    @WithMockUser(username = "sampleUser", authorities = "USER")
    public void testGetTurn() throws Exception {
        reset(gameService);
        // Configurar el comportamiento del servicio mock
        String code="testtest";

        User u = new User();
        u.setName("sample");
        List<Dwarf> dwarves = new ArrayList<>();
        g1.setDwarves(dwarves);

        when(us.findCurrentUser()).thenReturn(u);
        when(gameService.checkRoundNeedsChange(any(), any())).thenReturn(false);
        when(gameService.getGameByCode(code)).thenReturn(g1);
        when(gameService.checkPlayerInGameAndGameExists(g1)).thenReturn(true);

        mockMvc.perform(get(BASE_URL+"/play/"+code+"/isMyTurn").with(csrf())).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "sampleUser", authorities = "USER")
    public void testEndGame() throws Exception {
        reset(gameService);
        // Configurar el comportamiento del servicio mock
        String code="testtest";

        User u = new User();
        u.setName("sample");
        List<Dwarf> dwarves = new ArrayList<>();
        g1.setDwarves(dwarves);

        when(gameService.getGameByCode(code)).thenReturn(g1);
        when(gameService.checkPlayerInGameAndGameExists(g1)).thenReturn(true);

        mockMvc.perform(get(BASE_URL+"/play/"+code+"/isFinished").with(csrf())).andExpect(status().isOk());
    }



    /* 
    @Test
    public void testJoinGame() {
        // Configurar el comportamiento del servicio mock
        String gameCode = "TEST123";
        Game mockGame = new Game();
        when(gameService.getGameByCode(gameCode)).thenReturn(mockGame);

        User mockUser = new User();
        when(userService.findCurrentUser()).thenReturn(mockUser);

        // Ejecutar el método del controlador y verificar el resultado
        ResponseEntity<Game> result = gameRestController.joinGame(gameCode);
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Código de estado incorrecto");

        // Verificar que se haya creado un nuevo jugador correctamente
        verify(playerService, times(1)).savePlayer(any(Player.class));
    }
    /*
     * @Test
     * public void testSpecialOrder() {
     * // Configurar objetos de prueba
     * Game game = new Game();
     * // Configurar otros objetos según sea necesario
     * 
     * // Simular comportamiento de servicios
     * when(gameService.getGameByCode(anyString())).thenReturn(game);
     * when(gameService.checkPlayerInGameAndGameExists(any(Game.class))).thenReturn(
     * true);
     * 
     * // Configurar el usuario actual
     * when(userService.findCurrentUser()).thenReturn(new User());
     * 
     * // Configurar el jugador actual
     * Player currentPlayer = new Player();
     * List<Object> s = new ArrayList<Object>();
     * currentPlayer.setGold(10);
     * currentPlayer.setIron(10);
     * currentPlayer.setSteal(10);
     * currentPlayer.setObjects(s);
     * // Configurar el estado del jugador según sea necesario
     * when(playerService.getPlayerByUserAndGame(any(User.class),
     * any(Game.class))).thenReturn(currentPlayer);
     * 
     * // Llamar al método del controlador
     * SpecialCard sp = new SpecialCard();
     * Integer x = 1;
     * Integer y = 2;
     * Integer z= 2;
     * Object o = new Object();
     * o.setName("Axe");
     * sp.setName("Special Order");
     * 
     * ResponseEntity<Void> response = gameRestController
     * .handleSpecialAction2(
     * sp,
     * "testCode",
     * x, y, z, o);
     * // Verificar el resultado
     * assertEquals(HttpStatus.OK, response.getStatusCode());
     * // Verificar otros aspectos según sea necesario
     * }
     * 
     */
}
