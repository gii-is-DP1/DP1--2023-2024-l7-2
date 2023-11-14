package org.springframework.samples.petclinic.cardDeck;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.stereotype.Repository;

import jakarta.validation.Valid;

@Repository
public interface CardDeckRepository extends CrudRepository<CardDeck, Integer> {

    List<CardDeck> findAll();

    @Query("SELECT cd FROM CardDeck cd WHERE cd.id = ?1")
    CardDeck getCardDeckById(Integer id);

    //CardDeck updateCardDeck(@Valid CardDeck cd, int cardDeckId);

    //List<Card> getTwoCards(Integer id);

}