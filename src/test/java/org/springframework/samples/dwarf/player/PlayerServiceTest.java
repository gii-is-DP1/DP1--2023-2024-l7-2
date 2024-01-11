package org.springframework.samples.dwarf.player;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.CardService;
import org.springframework.samples.dwarf.card.CardType;
import org.springframework.samples.dwarf.game.Game;
import org.springframework.samples.dwarf.player.Player;
import org.springframework.samples.dwarf.player.PlayerRepository;
import org.springframework.samples.dwarf.player.PlayerService;
import org.springframework.samples.dwarf.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private CardService cardService;

    @InjectMocks
    private PlayerService playerService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPlayers() {
        List<Player> players = new ArrayList<>();
        when(playerRepository.findAll()).thenReturn(players);

        List<Player> result = playerService.getPlayers();

        assertNotNull(result);
        assertEquals(players, result);
        verify(playerRepository, times(1)).findAll();
    }

    @Test
    public void testGetByName() {
        Player player = new Player();
        player.setName("John");

        when(playerRepository.findByName("John")).thenReturn(player);

        Player retrievedPlayer = playerService.getByName("John");

        assertNotNull(retrievedPlayer);
        assertEquals("John", retrievedPlayer.getName());
    }
/*
    @Test
    public void testGetPlayerByUserAndGame() {
        User user = new User();
        Game game = new Game();
        Player player = new Player();

        when(playerRepository.findByUserAndGame(user, game)).thenReturn(player);

        Player result = playerService.getPlayerByUserAndGame(user, game);

        assertEquals(player, result);
        verify(playerRepository, times(1)).findByUserAndGame(user, game);
    }*/

    @Test
    public void testSavePlayer() {
        Player player = new Player();

        when(playerRepository.save(any(Player.class))).thenReturn(player);

        Player result = playerService.savePlayer(player);

        assertEquals(player, result);
        verify(playerRepository, times(1)).save(player);
    }

    @Test
    public void testGetById() {
        Player player = new Player();
        player.setId(1);

        when(playerRepository.findById(1)).thenReturn(Optional.of(player));

        Player retrievedPlayer = playerService.getById(1);

        assertNotNull(retrievedPlayer);
        assertEquals(1, retrievedPlayer.getId(), "La tarjeta recuperada no coincide");
    }

    @Test
    public void testGetPlayersByColor() {
        // Crea una lista de jugadores para simular la respuesta de la base de datos
        Player player = new Player();
        player.setId(1);
        player.setName("John");
        player.setColor("Red");

        // Configura el comportamiento del repositorio para devolver la lista cuando se
        // llama a findByColor
        when(playerRepository.findByColor("Red")).thenReturn(player);

        // Llama al método en tu servicio que utiliza el repositorio findByColor
        Player retrievedPlayers = playerService.getPlayersByColor("Red");

        // Verifica que se obtenga un resultado no nulo y que la lista tenga el tamaño
        // esperado
        assertNotNull(retrievedPlayers);
        assertEquals("Red", retrievedPlayers.getColor());
    }



    @Test
    public void testInitialize() {
        String username = "TestUser";

        Player initializedPlayer = playerService.initialize(username);

        assertNotNull(initializedPlayer);
        assertEquals(username, initializedPlayer.getName());
        assertEquals(0, initializedPlayer.getSteal());
        assertEquals(0, initializedPlayer.getGold());
        assertEquals(0, initializedPlayer.getIron());
        assertEquals(0, initializedPlayer.getMedal());
        
        
        verify(playerRepository, never()).save(any(Player.class));
    }
/*
    @Test
    public void testStatusChangeMC() {
        Player player = new Player();
        Card card = new Card();
        CardType cT = new CardType();
        cT.setName("Other");
        card.setCardType(cT);
        card.setTotalGold(5);
        card.setTotalIron(3);
        card.setTotalSteal(2);
        card.setTotalMedals(1);

        // when(cardService.getById(anyInt())).thenReturn(card);
        when(playerRepository.save(any(Player.class))).thenReturn(player);

        Player result = playerService.statusChangeMC(player, card);

        assertEquals(5, (int) result.getGold());
        assertEquals(3, (int) result.getIron());
        assertEquals(2, (int) result.getSteal());
        assertEquals(1, (int) result.getMedal());
        verify(playerRepository, times(1)).save(player);
    }*/

}