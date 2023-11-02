package org.springframework.samples.petclinic.card;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialCardRepository extends CrudRepository<SpecialCard, Integer> {
    List<SpecialCard> findAll();

    public SpecialCard findByName(String name);
}
