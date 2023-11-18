package org.springframework.samples.petclinic.player;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.user.User;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    public void testFindAll() {
        // Given
        Player player1 = new Player();
        player1.setName("Player1");
        player1.setColor("Red");
        playerRepository.save(player1);

        Player player2 = new Player();
        player2.setName("Player2");
        player2.setColor("Blue");
        playerRepository.save(player2);

        // When
        List<Player> players = playerRepository.findAll();

        // Then
        assertEquals(2, players.size());
        assertTrue(players.contains(player1));
        assertTrue(players.contains(player2));
    }

    @Test
    public void testFindByName() {
        // Given
        Player player = new Player();
        player.setName("TestPlayer");
        player.setColor("Green");
        playerRepository.save(player);

        // When
        Player result = playerRepository.findByName("TestPlayer");

        // Then
        assertNotNull(result);
        assertEquals("TestPlayer", result.getName());
        assertEquals("Green", result.getColor());
    }

    @Test
    public void testFindByColor() {
        // Given
        Player player = new Player();
        player.setName("Player3");
        player.setColor("Yellow");
        playerRepository.save(player);

        // When
        Player result = playerRepository.findByColor("Yellow");

        // Then
        assertNotNull(result);
        assertEquals("Player3", result.getName());
        assertEquals("Yellow", result.getColor());
    }

    @Test
    public void testFindByUserAndGame() {
        // Given
        Game game = new Game();

        User user = new User();
        user.setUsername("Pepe");

        Player player = new Player();
        player.setName("Player4");
        player.setColor("Purple");
        player.setGame(game);
        player.setUser(user);

        // When
        playerRepository.save(player);

        // Then
        Player result = playerRepository.findByUserAndGame(user, game);

        assertNotNull(result);
        assertEquals("Player4", result.getName());
        assertEquals("Purple", result.getColor());
        assertEquals(user, result.getUser());
        assertEquals(game, result.getGame());
    }
}