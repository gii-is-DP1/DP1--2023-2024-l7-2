package org.springframework.samples.dwarf.game;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.dwarf.user.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT g FROM Game g WHERE g.isPublic = true")
    Page<Game> findAllPublicGames(Pageable pageable);

    @Query("SELECT g FROM Game g WHERE g.userWinner =?1")
    List<Game> findAllWinnedGames(User u);

    @Query("SELECT DISTINCT g FROM Game g JOIN g.players p WHERE p.user.username = :username")
    List<Game> findGamesByUserName(String username);
    

}