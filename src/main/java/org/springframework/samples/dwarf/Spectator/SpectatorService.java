package org.springframework.samples.dwarf.Spectator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.dwarf.card.CardService;
import org.springframework.samples.dwarf.game.GameService;
import org.springframework.samples.dwarf.player.Player;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class SpectatorService {

    SpectatorRepository repo;
    CardService cs;
    GameService gs;

    @Autowired
    public SpectatorService(SpectatorRepository repo, CardService cs, GameService gs) {
        this.repo = repo;
        this.cs = cs;
        this.gs = gs;
    }

    @Transactional(readOnly = true)
    public List<Spectator> getSpectators() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Spectator getByName(String name) {
        return repo.findByName(name);
    }

    @Transactional
    public Spectator saveSpectator(@Valid Spectator newSpectator) {
        return repo.save(newSpectator);
    }

    @Transactional(readOnly = true)
    public Spectator getById(int id) {
        Optional<Spectator> result = repo.findById(id);
        return result.isPresent() ? result.get() : null;
    }

    /*
     * @Transactional(readOnly = true)
     * public Player getPlayersByName(String name) {
     * return repo.findByName(name);
     * }
     */

    @Transactional(readOnly = true)
    public String getRandomColor(List<Player> players) {

        ArrayList<String> colours = new ArrayList<String>();
        colours.addAll(List.of("red", "blue", "green", "magenta", "orange", "pink", "purple", "pink", "cyan", "brown"));
        Collections.shuffle(colours);


        players.forEach(p -> colours.remove(p.getColor()));
        return colours.get(0);
    }

    // Doesn't save the player
    @Transactional(readOnly = true)
    public Spectator initialize(String username) {
        Spectator p = new Spectator();
            
        p.setName(username);
        return p;
    }

}
