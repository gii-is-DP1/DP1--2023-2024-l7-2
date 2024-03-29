package org.springframework.samples.dwarf.card;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends CrudRepository<Card, Integer> {
    List<Card> findAll();

    public List<Card> findByName(String name);
}
