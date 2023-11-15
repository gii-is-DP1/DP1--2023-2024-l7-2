package org.springframework.samples.petclinic.player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.CardService;
import org.springframework.samples.petclinic.card.CardType;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
    private PlayereService playerService;

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
    public void testGetPlayerByUserAndGame() {
        User user = new User();
        Game game = new Game();
        Player player = new Player();

        when(playerRepository.findByUserAndGame(user, game)).thenReturn(player);

        Player result = playerService.getPlayerByUserAndGame(user, game);

        assertEquals(player, result);
        verify(playerRepository, times(1)).findByUserAndGame(user, game);
    }

    @Test
    public void testSavePlayer() {
        Player player = new Player();

        when(playerRepository.save(any(Player.class))).thenReturn(player);

        Player result = playerService.savePlayer(player);

        assertEquals(player, result);
        verify(playerRepository, times(1)).save(player);
    }

    
    
    @Test
    public void testStatusChangeMC() {
        Player player = new Player();
        Card card = new Card();
        CardType cT  = new CardType();
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
    }

    
}