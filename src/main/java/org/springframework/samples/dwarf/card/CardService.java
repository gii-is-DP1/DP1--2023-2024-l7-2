package org.springframework.samples.dwarf.card;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.dwarf.player.Player;
import org.springframework.samples.dwarf.object.Object;
import org.springframework.samples.dwarf.player.PlayerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class CardService {

    CardRepository repo;
    PlayerService ps;

    @Autowired
    public CardService(CardRepository repo, PlayerService ps) {
        this.repo = repo;
        this.ps = ps;
    }

    @Transactional(readOnly = true)
    public List<Card> getCards() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Card getById(int id) {
        Optional<Card> result = repo.findById(id);
        return result.isPresent() ? result.get() : null;
    }

    @Transactional
    public Card saveCard(@Valid Card newCard) {
        return repo.save(newCard);
    }

    @Transactional
    public void deleteCardById(int id) {
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Card getCardByName(String name) {
        return repo.findByName(name).get(0);
    }

    @Transactional(readOnly = true)
    public Boolean canApplyCard(Player p, Card c) {
        if (c.getTotalGold() * -1 > p.getGold()) {
            return false;
        } else if (c.getTotalSteal() * -1 > p.getSteal()) {
            return false;
        } else if (c.getTotalIron() * -1 > p.getIron()) {
            return false;
        } else if (c.getTotalMedals() * -1 > p.getMedal()) {
            return false;
        } else if (c.getObject() != null) {
            if (p.getObjects().contains(c.getObject())) {
                return false;
            }
        }

        return true;
    }

    /* FUNCIONES CARTAS NORMALES */

    public void updateMaterialsSingleAction(Player p, Card c) {
        if (canApplyCard(p, c)) {
            p.setGold(p.getGold() + c.getTotalGold());
            p.setIron(p.getIron() + c.getTotalIron());
            p.setSteal(p.getSteal() + c.getTotalSteal());
            p.setMedal(p.getMedal() + c.getTotalMedals());

            ps.savePlayer(p);
        }
    }


    /* FUNCIONES ORCOS */

    @Transactional
    public void adwardMedalSingleAction(Player p, Card c) {
            p.setMedal(p.getMedal() + 1);

            ps.savePlayer(p);
    }

    @Transactional
    public void orcCardKnockersAction(List<Player> players ) {
        for (Player p : players) {
            p.setIron(p.getIron() - 1);
            if (p.getIron() < 0) {
                p.setIron(0);
            }
            ps.savePlayer(p);
        }
    }

    @Transactional
    public void orcCardSidheAction(List<Player> players) {

        for (Player p : players) {

            p.setGold(p.getGold() - 2);
            if (p.getGold() == -1) {
                p.setIron(p.getIron() + 1);
            } else if (p.getGold() == -2) {
                p.setIron(p.getIron());

            } else {
                p.setIron(p.getIron() + 2);
                if (p.getIron() < 0) {
                    p.setIron(0);
                }
            }
            if (p.getGold() < 0) {
                p.setGold(0);
            }
            ps.savePlayer(p);
        }

    }

    @Transactional
    public void orcCardDragonAction(List<Player> players) {
        for (Player p : players) {
            p.setGold(p.getGold() - 1);
            if (p.getGold() < 0) {
                p.setGold(0);
            }
            ps.savePlayer(p);
        }

    }

    @Transactional
    public void orcCardGreatDragonAction(List<Player> players) {

        for (Player p : players) {
            p.setGold(0);
            ps.savePlayer(p);
        }
    }

    /* FUNCIONES FORJAR */

    @Transactional
    public void forjarSingleAction(Player p, Card c) {
    
        if (canApplyCard(p, c)) {
            p.setGold(p.getGold() + c.getTotalGold());
            p.setIron(p.getIron() + c.getTotalIron());
            p.setSteal(p.getSteal() + c.getTotalSteal());
            p.setMedal(p.getMedal() + c.getTotalMedals());

            ArrayList<Object> objects = new ArrayList<>();
            objects.addAll(p.getObjects());
            objects.add(c.getObject());
            p.setObjects(objects);
            ps.savePlayer(p);
        }
    }

}
