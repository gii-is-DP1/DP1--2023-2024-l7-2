package org.springframework.samples.dwarf.card;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.dwarf.game.Game;
import org.springframework.samples.dwarf.player.Player;
import org.springframework.samples.dwarf.player.PlayerService;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CardServiceTest {

    @Autowired
    private CardService cardService;

    @Autowired
    private PlayerService playerService;

    @Test
    public void testGetCards() {
        List<Card> cards = cardService.getCards();
        assertNotNull(cards);

    }

    @Test
    public void testGetById() {

        Integer id = 1;
        String name = "Iron Seam";
        Integer position = 1;

        Card retrievedCard = cardService.getById(id);
        assertNotNull(retrievedCard);
        assertEquals(id, retrievedCard.getId());
        assertEquals(name, retrievedCard.getName());
        assertEquals(position, retrievedCard.getPosition());

    }

    @Test
    public void testSaveCard() {
        Card newCard = new Card();

        CardType newCardType = cardService.getById(1).getCardType();

        newCard.setName("sample name");
        newCard.setDescription("sample description");
        newCard.setPosition(2);
        newCard.setCardType(newCardType);

        Card savedCard = cardService.saveCard(newCard);

        assertNotNull(savedCard.getId());

        assertEquals(savedCard.getId(),100);

    }

    @Test
    public void testDeleteCardById() {

        int existingCardId = 1;
        Card existingCard = cardService.getById(existingCardId);

        assertNotNull(existingCard);

        cardService.deleteCardById(existingCardId);

        assertNull(cardService.getById(existingCardId));
    }

    @Test
    public void testGetCardByName() {

        Card existingCard = cardService.getCards().get(0);
        String cardName = existingCard.getName();

        Card retrievedCard = cardService.getCardByName(cardName);

        assertNotNull(retrievedCard);
        assertEquals(cardName, retrievedCard.getName());

    }


    @Test
    public void testOrcCardKnockersAction() {
        // Datos de prueba
        Player player1 = new Player();
        player1.setIron(5);

        Player player2 = new Player();
        player2.setIron(0);

        player1 = playerService.savePlayer(player1);
        player2 = playerService.savePlayer(player2);

        List<Player> players = Arrays.asList(player1, player2);

        // Llamada al método que queremos probar
        cardService.orcCardKnockersAction(players);

        player1 = playerService.getById(player1.getId());
        player2 = playerService.getById(player2.getId());

        // Verificaciones
        assertEquals(4, player1.getIron()); // 5 - 1 = 4
        assertEquals(0, player2.getIron()); // La cantidad de hierro no debería ser menor que 0
    }
/*
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
    }*/
}
