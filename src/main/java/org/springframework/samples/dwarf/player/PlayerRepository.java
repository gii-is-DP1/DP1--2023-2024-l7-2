package org.springframework.samples.dwarf.player;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Integer> {
    List<Player> findAll();

    public Player findByName(String name);

    public Player findByColor(String color);

}
