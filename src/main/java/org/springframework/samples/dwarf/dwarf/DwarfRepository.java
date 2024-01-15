package org.springframework.samples.dwarf.dwarf;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DwarfRepository extends CrudRepository<Dwarf, Integer> {

    List<Dwarf> findAll();

    @Query("SELECT d FROM Dwarf d WHERE d.player.user.username = :username")
    List<Dwarf> findDwarvesByUserName(String username);
}
