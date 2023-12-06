package org.springframework.samples.dwarf.location;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends CrudRepository<Location,Integer> {
    
    List<Location> findAll();

    Location findById(int id);
}