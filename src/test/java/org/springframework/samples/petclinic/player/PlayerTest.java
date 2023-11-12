package org.springframework.samples.petclinic.player;

import org.junit.Test;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.user.User;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PlayerTest {

    @Test
    public void testPlayerColorNotBlank() {
        Player player = new Player();
        player.setColor("Red");

        assertEquals("Red", player.getColor());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlayerColorBlank() {
        Player player = new Player();
        player.setColor("");

        // This should throw an IllegalArgumentException due to @NotBlank validation
    }

    @Test
    public void testPlayerStealGoldIronMedalNotNull() {
        Player player = new Player();
        player.setSteal(10);
        player.setGold(20);
        player.setIron(5);
        player.setMedal(30);

        assertNotNull(player.getSteal());
        assertNotNull(player.getGold());
        assertNotNull(player.getIron());
        assertNotNull(player.getMedal());
    }

    @Test
    public void testPlayerUserNotNull() {
        Player player = new Player();
        User mockUser = mock(User.class);
        player.setUser(mockUser);

        assertNotNull(player.getUser());
    }

    @Test
    public void testPlayerGameNotNull() {
        Player player = new Player();
        Game mockGame = mock(Game.class);
        player.setGame(mockGame);

        assertNotNull(player.getGame());
    }

    @Test
    public void testPlayerInitialization() {
        Player player = new Player();

        // Ensure that the player object is not null after initialization
        assertNotNull(player);
    }

    @Test
    public void testPlayerColor() {
        Player player = new Player();
        player.setColor("Blue");

        assertEquals("Blue", player.getColor());
    }

    @Test
    public void testPlayerStealGoldIronMedalValues() {
        Player player = new Player();
        player.setSteal(10);
        player.setGold(20);
        player.setIron(5);
        player.setMedal(30);

        assertEquals(Integer.valueOf(10), player.getSteal());
        assertEquals(Integer.valueOf(20), player.getGold());
        assertEquals(Integer.valueOf(5), player.getIron());
        assertEquals(Integer.valueOf(30), player.getMedal());
    }

    @Test
    public void testPlayerUser() {
        Player player = new Player();
        User mockUser = mock(User.class);
        player.setUser(mockUser);

        assertEquals(mockUser, player.getUser());
    }

    @Test
    public void testPlayerGame() {
        Player player = new Player();
        Game mockGame = mock(Game.class);
        player.setGame(mockGame);

        assertEquals(mockGame, player.getGame());
    }

}