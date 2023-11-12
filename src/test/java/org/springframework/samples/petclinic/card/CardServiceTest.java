package org.springframework.samples.petclinic.card;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CardServiceTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private CardService cardService;

  @Test
  public void testGetCards() {
      
      Card card = new Card();
      card.setName("Test Card");
      entityManager.persist(card);
      entityManager.flush();

      
      List<Card> cards = cardService.getCards();

      
      assertThat(cards.size()).isGreaterThan(0);
  }

  @Test
  public void testGetById() {
      // Guarda una tarjeta en la base de datos
      Card card = new Card();
      card.setName("Test Card");
      entityManager.persist(card);
      entityManager.flush();

     
      Card foundCard = cardService.getById(card.getId());

      // Verifica que la tarjeta encontrada es la correcta
      assertThat(foundCard.getName()).isEqualTo("Test Card");
  }

  // Puedes agregar pruebas similares para los otros métodos en CardService
}
