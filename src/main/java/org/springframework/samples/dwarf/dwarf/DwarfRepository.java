package org.springframework.samples.dwarf.dwarf;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DwarfRepository extends CrudRepository<Dwarf, Integer> {

    List<Dwarf> findAll();
    // public Dwarf findByColor(String color);
}
