package org.springframework.samples.dwarf.game;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface GameRepository extends CrudRepository<Game, Integer> {
    List<Game> findAll();

    List<Game> findByNameContains(String pattern);

    List<Game> findByStart(LocalDateTime start);

    List<Game> findByFinish(LocalDateTime finish);

    List<Game> findByCode(String code);

    List<Game> findByFinishIsNotNull();

    List<Game> findByFinishIsNullAndStartIsNotNull();


    @Query("SELECT g FROM Game g WHERE g.isPublic = true")
    List<Game> findAllPublicGames();
}