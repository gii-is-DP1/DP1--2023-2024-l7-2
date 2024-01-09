package org.springframework.samples.dwarf.statistic;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.dwarf.game.Game;
import org.springframework.samples.dwarf.game.GameRepository;
import org.springframework.samples.dwarf.user.User;
import org.springframework.samples.dwarf.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class AchievementService {

    AchievementRepository repo;
    UserRepository userRepo;
    GameRepository gameRepo;

    @Autowired
    public AchievementService(AchievementRepository repo, UserRepository userRepo, GameRepository gameRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.gameRepo = gameRepo;
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
    public List<Achievement> getAchievementByUserName(String name) {
        return userRepo.findAchievemenByUserName(name);
    }

    @Transactional(readOnly = true)
    public List<Game> getAllWinnedGames(String name) {
        return gameRepo.findAllWinnedGames(name);
    }

    @Transactional(readOnly = true)
    public List<Game> getPlayedGames(String username) {
        return gameRepo.findGamesByUserName(username);
    }
}
