package org.springframework.samples.dwarf.location;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends CrudRepository<Location,Integer> {
    
    public List<Location> findAll();

    Optional<Location> findById(int id);
}