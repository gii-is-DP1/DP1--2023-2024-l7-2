package org.springframework.samples.dwarf.player;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.CardService;
import org.springframework.samples.dwarf.game.Game;
import org.springframework.samples.dwarf.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class PlayereService {

    PlayerRepository repo;
    CardService cs;

    @Autowired
    public PlayereService(PlayerRepository repo, CardService cs) {
        this.repo = repo;
        this.cs = cs;
    }

    @Transactional(readOnly = true)
    public List<Player> getPlayers() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Player getByName(String name) {
        return repo.findByName(name);
    }

    @Transactional(readOnly = true)
    public Player getPlayerByUserAndGame(User u, Game g) {
        return repo.findByUserAndGame(u, g);
    }

    @Transactional
    public Player savePlayer(@Valid Player newPlayer) {
        return repo.save(newPlayer);
    }

    @Transactional(readOnly = true)
    public Player getById(int id) {
        Optional<Player> result = repo.findById(id);
        return result.isPresent() ? result.get() : null;
    }

    @Transactional(readOnly = true)
    public Player getPlayersByColor(String color) {
        return repo.findByColor(color);
    }

    /*
     * @Transactional(readOnly = true)
     * public Player getPlayersByName(String name) {
     * return repo.findByName(name);
     * }
     */

    @Transactional
    public Player statusChangeMC(@Valid Player p, @Valid Card c) {
        if (c.getCardType().toString().equals("Other")) {

            int totalGold = c.getTotalGold() != null ? c.getTotalGold() : 0;
            int totalIron = c.getTotalIron() != null ? c.getTotalIron() : 0;
            int totalSteal = c.getTotalSteal() != null ? c.getTotalSteal() : 0;
            int totalMedals = c.getTotalMedals() != null ? c.getTotalMedals() : 0;

            p.setGold((p.getGold() != null ? p.getGold() : 0) + totalGold);
            p.setIron((p.getIron() != null ? p.getIron() : 0) + totalIron);
            p.setSteal((p.getSteal() != null ? p.getSteal() : 0) + totalSteal);
            p.setMedal((p.getMedal() != null ? p.getMedal() : 0) + totalMedals);
        }

        if (p.getGold() != null && p.getGold() < 0) {
            p.setGold(0);
        }

        if (p.getIron() != null && p.getIron() < 0) {
            p.setIron(0);
        }

        if (p.getSteal() != null && p.getSteal() < 0) {
            p.setSteal(0);
        }

        if (p.getMedal() != null && p.getMedal() < 0) {
            p.setMedal(0);
        }

        return repo.save(p);
    }

}
