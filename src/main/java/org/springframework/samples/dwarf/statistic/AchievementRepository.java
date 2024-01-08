package org.springframework.samples.dwarf.statistic;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.dwarf.game.Game;
import org.springframework.samples.dwarf.user.User;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepository extends CrudRepository<Achievement, Integer> {
    List<Achievement> findAll();

    public Achievement findByName(String name);

    public Achievement findByUserName(String username);

    @Query("SELECT g FROM Game g WHERE g.winner_id =?1")
    List<Game> findAllWinnedGames(String userId);
    @Query("SELECT g FROM Game g WHERE ?1 in g.players")
    List<Game> findAllPlayedGames(User user);
}
