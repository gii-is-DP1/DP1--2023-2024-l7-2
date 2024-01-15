package org.springframework.samples.dwarf.dwarf;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.player.Player;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class DwarfService {

    DwarfRepository repo;

    @Autowired
    public DwarfService(DwarfRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Dwarf> getDwarfs() {
        return repo.findAll();
    }

    @Transactional
    public List<Dwarf> getDwarvesUsername(String username) {
        return repo.findDwarvesByUserName(username);
    }

    @Transactional
    public Dwarf saveDwarf(@Valid Dwarf newDwarf) {
        return repo.save(newDwarf);
    }

    @Transactional(readOnly = true)
    public Dwarf getById(int id) {
        Optional<Dwarf> result = repo.findById(id);
        return result.isPresent() ? result.get() : null;
    }

    /*
     * @Transactional(readOnly = true)
     * public Dwarf getDwarfsByColor(String color) {
     * return repo.findByColor(color);
     * }
     */

    @Transactional
    public void deleteDwarf(Dwarf d) {
        repo.delete(d);
    }

    @Transactional
    public Dwarf genAndSave(Player p, Integer pos, Card c) {
        Dwarf dwarf = new Dwarf();

        dwarf.setPlayer(p);
        dwarf.setRound(pos);
        dwarf.setCard(c);

        dwarf = saveDwarf(dwarf);
        return dwarf;
    }

    @Transactional
    public void updateDwarvesWhenUpdatedCards(List<Dwarf> dwarves, List<Card> cards) {
        for (Dwarf d: dwarves) {
            Card originalCard = d.getCard();
            Integer position = originalCard.getPosition();
            Card newCard = cards.get(position - 1);
            if (!originalCard.getName().equals(newCard.getName())) {
                d.setCard(newCard);
                saveDwarf(d);
            }
        }
    }


}
