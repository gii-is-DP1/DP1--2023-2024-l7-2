package org.springframework.samples.dwarf.statistic;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.dwarf.game.Game;
import org.springframework.samples.dwarf.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class AchievementService {

    AchievementRepository repo;

    @Autowired
    public AchievementService(AchievementRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    List<Achievement> getAchievements() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Achievement getById(int id) {
        Optional<Achievement> result = repo.findById(id);
        return result.isPresent() ? result.get() : null;
    }

    @Transactional
    public Achievement saveAchievement(@Valid Achievement newAchievement) {
        return repo.save(newAchievement);
    }

    @Transactional
    public void deleteAchievementById(int id) {
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Achievement getAchievementByName(String name) {
        return repo.findByName(name);
    }

    @Transactional(readOnly = true)
    public Achievement getAchievementByUserName(String name) {
        return repo.findByUserName(name);
    }

    @Transactional(readOnly = true)
    public List<Game> getAllWinnedGames(String name) {
        return repo.findAllWinnedGames(name);
    }

    @Transactional(readOnly = true)
    public List<Game> getPlayedGames(User user) {
        return repo.findAllPlayedGames(user);
    }
}
