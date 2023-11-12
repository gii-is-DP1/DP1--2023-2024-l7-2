package org.springframework.samples.petclinic.card;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CardRepositoryTest {


   @Autowired
   private CardRepository cardRepository;

   @Test
   public void testFindAll() {
       Card card = new Card();
       card.setName("Test Card");
       card.setId(2);
       List<Card> cards = cardRepository.findAll();
       assertThat(cards.size()).isGreaterThan(0);
   }

   @Test
   public void testFindByName() {
       Card foundCard = cardRepository.findByName("Test Card");
       assertThat(foundCard.getName()).isEqualTo("Test Card");
   }
}
