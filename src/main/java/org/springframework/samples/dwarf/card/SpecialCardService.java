package org.springframework.samples.dwarf.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.dwarf.dwarf.Dwarf;
import org.springframework.samples.dwarf.dwarf.DwarfService;
import org.springframework.samples.dwarf.game.Game;
import org.springframework.samples.dwarf.game.SpecialCardRequestHandler;
import org.springframework.samples.dwarf.location.Location;
import org.springframework.samples.dwarf.location.LocationService;
import org.springframework.samples.dwarf.mainboard.MainBoard;
import org.springframework.samples.dwarf.mainboard.MainBoardService;
import org.springframework.samples.dwarf.player.Player;
import org.springframework.samples.dwarf.player.PlayerService;
import org.springframework.samples.dwarf.object.Object;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class SpecialCardService {

    private final SpecialCardRepository repo;
    private final DwarfService dwService;
    private final PlayerService plService;
    private final LocationService locService;
    private final MainBoardService mbService;

    private final String ORC_CARD = "OrcCard";

    @Autowired
    public SpecialCardService(SpecialCardRepository repo, DwarfService dwService, PlayerService plService
        , LocationService locService, MainBoardService mbService) {
        this.repo = repo;
        this.dwService = dwService;
        this.plService = plService;
        this.locService = locService;
        this.mbService = mbService;
    }

    @Transactional(readOnly = true)
    public List<SpecialCard> getSpecialCards() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public SpecialCard getById(int id) {
        Optional<SpecialCard> result = repo.findById(id);
        return result.isPresent() ? result.get() : null;
    }

    @Transactional
    public SpecialCard saveSpecialCard(@Valid SpecialCard newCard) {
        return repo.save(newCard);
    }

    @Transactional
    public void deleteSpecialCardById(int id) {
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public SpecialCard getSpecialCardByName(String name) {
        return repo.findByName(name);
    }

    @Transactional
    public Game handleIfBothDwarvesAreUsed(Game g, Player p, Integer round, Boolean usesBothDwarves) {
        if (usesBothDwarves) {
            Dwarf dwarf1 = dwService.genAndSave(p, round, null);
            Dwarf dwarf2 = dwService.genAndSave(p, round, null);

            List<Dwarf> gameDwarves = g.getDwarves();
            gameDwarves.add(dwarf1);
            gameDwarves.add(dwarf2);
            g.setDwarves(gameDwarves);
        } else {
            if (p.getMedal() > 4) {
                p.setMedal(p.getMedal() - 4);
                plService.savePlayer(p);
            } else {
                // TODO: create error
            }

            Dwarf dwarf1 = dwService.genAndSave(p, round, null);

            List<Dwarf> gameDwarves = g.getDwarves();
            gameDwarves.add(dwarf1);
            g.setDwarves(gameDwarves);
        }

        return g;
    }

    @Transactional
    public Game musterAnArmyAction(Game g, Integer round) {
        List<Card> gameCards = g.getMainBoard().getCards();
        ArrayList<Dwarf> gameDwarves = new ArrayList<>(g.getDwarves());
        for (Card c : gameCards) {
            if (c.getCardType().getName().equals(ORC_CARD)) {
                Dwarf d = dwService.genAndSave(null, round, c);
                gameDwarves.add(d);
            }
        }
        g.setDwarves(gameDwarves);
        return g;
    }

    @Transactional
    public Game apprenticeAction(Game g,Player p, Integer round, Integer selectedPosition, List<Dwarf> roundDwarvesApprentice) {
        for (Dwarf d : roundDwarvesApprentice) {

            if (!d.getPlayer().equals(p) && d.getCard().getPosition().equals(selectedPosition)) {
                // Coloca un nuevo enano en la misma posición
                Dwarf newDwarf = dwService.genAndSave(p, round, d.getCard());

                List<Dwarf> gameDwarvesApprentice = g.getDwarves();
                gameDwarvesApprentice.add(newDwarf);
                g.setDwarves(gameDwarvesApprentice);
                break;
            }
        }
        return g;
    }

    @Transactional
    public void specialOrderAction(Player p, Integer selectedGold, Integer selectedIron, 
        Integer selectedSteal, Object selectedObject) {

        if (selectedGold != null && selectedIron != null
                && selectedSteal != null && selectedObject != null
                && selectedGold + selectedIron + selectedSteal == 5
                && selectedGold > 0 && selectedIron > 0 && selectedSteal > 0) {

            List<Object> playerObjects = p.getObjects();
            // Update player's state
            if (selectedGold > p.getGold() || selectedIron > p.getIron()
                    || selectedSteal > p.getSteal() || playerObjects.contains(selectedObject)) {
                // TODO: Create error
            }
            p.setGold(p.getGold() - selectedGold);
            p.setIron(p.getIron() - selectedIron);
            p.setSteal(p.getSteal() - selectedSteal);

            // Add the selected object
            playerObjects.add(selectedObject);
            p.setObjects(playerObjects);

            // Save the updated player
            plService.savePlayer(p);
        }
    }

    @Transactional
    public void sellAnItemAction(Player p, Integer selectedGold, Integer selectedIron, 
        Integer selectedSteal, Object selectedObject) {
        if (selectedGold != null && selectedIron != null
            && selectedSteal != null && selectedObject != null 
            && selectedGold + selectedIron + selectedSteal == 5 
            && selectedGold > 0 && selectedIron > 0 && selectedSteal > 0) {

            List<Object> playerObjects = p.getObjects();
            // Update player's state
            p.setGold(p.getGold() + selectedGold);
            p.setIron(p.getIron() + selectedIron);
            p.setSteal(p.getSteal() + selectedSteal);

            // Remove the selected object
            if (!playerObjects.contains(selectedObject)) {
                // TODO: Create error
            }
            playerObjects.remove(selectedObject);
            p.setObjects(playerObjects);
            // Save the updated player
            plService.savePlayer(p);
        }
    }

    @Transactional
    public Game turnBackAction(Game g, Player p, Integer round, Integer selectedPosition, 
        List<Location> newLocationsTurnBack) {
    
        if (selectedPosition >= 1 && selectedPosition <= newLocationsTurnBack.size()) {
            Location selectedLocation = newLocationsTurnBack.get(selectedPosition - 1);

            Card removedCard = locService.removeLastCard(selectedLocation);
            if (removedCard != null) {
                List<Card> newCards = selectedLocation.getCards();
                Card newTopCard = newCards.get(newCards.size() - 1);

                Dwarf newDwarfTurnBack = dwService.genAndSave(p, round, newTopCard);

                List<Dwarf> gameDwarvesTurnBack = new ArrayList<>(g.getDwarves());
                gameDwarvesTurnBack.add(newDwarfTurnBack);
                g.setDwarves(gameDwarvesTurnBack);
            }
        } else {
            // TODO: create error
        }
        return g;
    }

    @Transactional
    public void pastGloriesAction(Integer selectedPosition, Card cardToBeOnTop, List<Location> locations) {
        if (selectedPosition != null && cardToBeOnTop != null && selectedPosition >= 1
            && selectedPosition <= 9) {

            Location selectedLocation = locations.get(selectedPosition - 1);

                locService.pastGloriesAction(selectedLocation, cardToBeOnTop);

            } else {
                // TODO: create error
            }
    }

    private final String SPECIAL_CARD_MUSTER_AN_ARMY = "Muster an army";
    private final String SPECIAL_CARD_SPECIAL_ORDER = "Special order";
    private final String SPECIAL_CARD_HOLD_A_COUNCIL = "Hold a council";
    private final String SPECIAL_CARD_COLLAPSE_THE_SHAFTS = "Collapse the Shafts";
    private final String SPECIAL_CARD_RUN_AMOK = "Run Amok";
    private final String SPECIAL_CARD_SELL_AN_ITEM = "Sell an item";
    private final String SPECIAL_CARD_APPRENTICE = "Apprentice";
    private final String SPECIAL_CARD_TURN_BACK = "Turn back";
    private final String SPECIAL_CARD_PAST_GLORIES = "Past Glories";
    @Transactional
    public Game resolveSpecialCard(Game g, Player p, MainBoard mb, SpecialCardRequestHandler request, 
        Integer round, List<Dwarf> roundDwarvesApprentice) {
        
        SpecialCard specialCard = request.getSpecialCard();
        Integer selectedGold = request.getSelectedGold();
        Integer selectedIron = request.getSelectedIron();
        Integer selectedSteal = request.getSelectedSteal();
        Object selectedObject = request.getSelectedObject();
        Integer selectedPosition = request.getPosition();
        Card cardToBeOnTop = request.getPastCard();
        List<Location> locations;
        // Ahora aplicamos la carta
        switch (specialCard.getName()) {
            case SPECIAL_CARD_MUSTER_AN_ARMY:
                g = musterAnArmyAction(g, round);
                break;
            case SPECIAL_CARD_SPECIAL_ORDER:

                specialOrderAction(p, selectedGold, selectedIron, selectedSteal, selectedObject);
                break;

            case SPECIAL_CARD_HOLD_A_COUNCIL:
                mb = mbService.holdACouncilAction(mb);
                break;

            case SPECIAL_CARD_COLLAPSE_THE_SHAFTS:
                mbService.collapseTheShaftsAction(mb);
                break;

            case SPECIAL_CARD_RUN_AMOK:
                mb = mbService.runAmokAction(mb);
                mb = mbService.saveMainBoard(mb);
                g.setMainBoard(mb);
                break;

            case SPECIAL_CARD_SELL_AN_ITEM:
                sellAnItemAction(p, selectedGold, selectedIron, selectedSteal, selectedObject);
                break;

            case SPECIAL_CARD_APPRENTICE:

                g = apprenticeAction(g, p, round, selectedPosition, roundDwarvesApprentice);
                break;

            case SPECIAL_CARD_TURN_BACK:
                locations = new ArrayList<>(mb.getLocations());

                g = turnBackAction(g, p, round, selectedPosition, locations);

                break;
            case SPECIAL_CARD_PAST_GLORIES:
                locations = new ArrayList<>(mb.getLocations());
                pastGloriesAction(selectedPosition, cardToBeOnTop, locations);

                break;

            default:
                break;
        }
        return g;
    }

}
