package org.springframework.samples.petclinic.player;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class PlayereService {

    PlayerRepository repo;

    @Autowired
    public PlayereService(PlayerRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    List<Player> getPlayers() {
        return repo.findAll();
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

    @Transactional(readOnly = true)
    public Player getPlayersByName(String name) {
        return repo.findByName(name);
    }

}
