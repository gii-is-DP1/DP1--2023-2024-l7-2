package org.springframework.samples.dwarf.spectator;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpectatorRepository extends CrudRepository<Spectator, Integer> {
    List<Spectator> findAll();

    public Spectator findByName(String name);
}
