package org.springframework.samples.petclinic.player;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.CardService;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.user.User;
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
    public Player getPlayerByUserAndGame(User u, Game g) {
        return repo.findByUserAndGame(u, g);
    }

    @Transactional
    public Player savePlayer(@Valid Player newPlayer) {
        return repo.save(newPlayer);
    }

    @Transactional
    public Player statusChangeMC(@Valid Player p, @Valid Card c) {
        if (c.getCardType().toString()== "Other"){
            p.setGold(p.getGold()+c.getTotalGold());
            p.setIron(p.getIron()+c.getTotalIron());
            p.setSteal(p.getSteal()+c.getTotalSteal());
            p.setMedal(p.getMedal()+c.getTotalMedals());
        }

        if (p.getGold() < 0) {
            p.setGold(0);   
        }

         if (p.getIron() < 0) {
            p.setIron(0);   
        }

         if (p.getSteal() < 0) {
            p.setSteal(0);   
        }

         if (p.getMedal() < 0) {
            p.setMedal(0);   
        }


        return repo.save(p);

            
        
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
