package org.springframework.samples.dwarf.cardDeck;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardDeckRepository extends CrudRepository<CardDeck, Integer> {

    List<CardDeck> findAll();

    @Query("SELECT cd FROM CardDeck cd WHERE cd.id = ?1")
    CardDeck getCardDeckById(Integer id);

}