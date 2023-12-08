package org.springframework.samples.dwarf.specialCardDeck;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialCardDeckRepository extends CrudRepository<SpecialCardDeck, Integer> {

    List<SpecialCardDeck> findAll();

    Optional<SpecialCardDeck> findById(Integer id);

    // SpecialCardDeck getSpecialCardDeckById(Integer id);

    // SpecialCardDeck updateSpecialCardDeck(@Valid SpecialCardDeck scd, int
    // specialCardDeckId);

    // SpecialCard getSpecialCard(Integer id);
}