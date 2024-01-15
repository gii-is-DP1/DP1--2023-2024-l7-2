package org.springframework.samples.dwarf.card;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.dwarf.dwarf.Dwarf;
import org.springframework.samples.dwarf.dwarf.DwarfService;
import org.springframework.samples.dwarf.exceptions.CannotUseCardException;
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

    private final Integer POSITION_MAX = 9;
    private final Integer POSITION_MIN = 1;

    private final String SPECIAL_CARD_MUSTER_AN_ARMY = "Muster an army";
    private final String SPECIAL_CARD_SPECIAL_ORDER = "Special order";
    private final String SPECIAL_CARD_HOLD_A_COUNCIL = "Hold a council";
    private final String SPECIAL_CARD_COLLAPSE_THE_SHAFTS = "Collapse the Shafts";
    private final String SPECIAL_CARD_RUN_AMOK = "Run Amok";
    private final String SPECIAL_CARD_SELL_AN_ITEM = "Sell an item";
    private final String SPECIAL_CARD_APPRENTICE = "Apprentice";
    private final String SPECIAL_CARD_TURN_BACK = "Turn back";
    private final String SPECIAL_CARD_PAST_GLORIES = "Past Glories";

    private final SpecialCardRepository repo;
    private final DwarfService dwService;
    private final PlayerService plService;
    private final LocationService locService;
    private final MainBoardService mbService;

    private final String ORC_CARD = "OrcCard";

    @Autowired
    public SpecialCardService(SpecialCardRepository repo, DwarfService dwService, PlayerService plService,
            LocationService locService, MainBoardService mbService) {
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
            plService.removeMedalsUsedSpecialCard(p);

            Dwarf dwarf1 = dwService.genAndSave(p, round, null);

            List<Dwarf> gameDwarves = g.getDwarves();
            gameDwarves.add(dwarf1);
            g.setDwarves(gameDwarves);
        }

        return g;
    }

    @Transactional
    public Game musterAnArmyAction(Game g, Integer round, List<Card> gameCards) {
        List<Dwarf> gameDwarves = g.getDwarves();
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
    public Game apprenticeAction(Game g, Player p, Integer round,
            Integer selectedPosition, List<Dwarf> roundDwarvesApprentice) {

        Boolean changed = false;
        for (Dwarf d : roundDwarvesApprentice) {

            if (!d.getPlayer().equals(p) && d.getCard().getPosition().equals(selectedPosition)) {
                // Coloca un nuevo enano en la misma posición
                Dwarf newDwarf = dwService.genAndSave(p, round, d.getCard());

                List<Dwarf> gameDwarvesApprentice = g.getDwarves();
                gameDwarvesApprentice.add(newDwarf);
                g.setDwarves(gameDwarvesApprentice);
                changed = true;
                break;
            }
        }
        if (!changed) {
            throw new CannotUseCardException("No user uses this position");
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
                throw new CannotUseCardException("Not enough resources");
            }
            p.setGold(p.getGold() - selectedGold);
            p.setIron(p.getIron() - selectedIron);
            p.setSteal(p.getSteal() - selectedSteal);

            // Add the selected object
            playerObjects.add(selectedObject);
            p.setObjects(playerObjects);

            // Save the updated player
            plService.savePlayer(p);
        } else {
            throw new CannotUseCardException("Not enough resources");
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
            Boolean containsObject = false;
            for (Object o : playerObjects) {
                if (o.getName().equals(selectedObject.getName())) {
                    containsObject = true;
                    selectedObject = o;
                    break;
                }
            }
            if (!containsObject) {
                throw new CannotUseCardException("Not enough resources");
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

        if (selectedPosition >= POSITION_MIN && selectedPosition <= POSITION_MAX) {
            Location selectedLocation = newLocationsTurnBack.get(selectedPosition - 1);

            List<Dwarf> thisRoundDwarves = g.getDwarves().stream().filter(d -> d.getRound() == round
                    && d.getPlayer() != null).toList();

            for (Dwarf d : thisRoundDwarves) {
                if (d.getCard() != null && d.getCard().getPosition() == selectedPosition) {
                    throw new CannotUseCardException("A user already uses this positions");
                }
            }

            Card removedCard = locService.removeLastCard(selectedLocation);
            if (removedCard != null) {
                List<Card> newCards = selectedLocation.getCards();
                Card newTopCard = newCards.get(newCards.size() - 1);

                Dwarf newDwarfTurnBack = dwService.genAndSave(p, round, newTopCard);

                List<Dwarf> gameDwarvesTurnBack = g.getDwarves();
                gameDwarvesTurnBack.add(newDwarfTurnBack);
                g.setDwarves(gameDwarvesTurnBack);
            }
        } else {
            throw new CannotUseCardException("Position does not exist");
        }
        return g;
    }

    @Transactional
    public void pastGloriesAction(Integer selectedPosition, Card cardToBeOnTop, List<Location> locations) {
        if (selectedPosition != null && cardToBeOnTop != null && selectedPosition >= POSITION_MIN
                && selectedPosition <= POSITION_MAX) {

            Location selectedLocation = locations.get(selectedPosition - 1);

            locService.pastGloriesAction(selectedLocation, cardToBeOnTop);

        } else {
            throw new CannotUseCardException("Not enough information to use this card");
        }
    }

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
        List<Card> cards;
        // Ahora aplicamos la carta
        switch (specialCard.getName()) {
            case SPECIAL_CARD_MUSTER_AN_ARMY:
                List<Card> gameCards = mb.getCards();
                g = musterAnArmyAction(g, round, gameCards);
                break;
            case SPECIAL_CARD_SPECIAL_ORDER:

                specialOrderAction(p, selectedGold, selectedIron, selectedSteal, selectedObject);
                break;

            case SPECIAL_CARD_HOLD_A_COUNCIL:
                mbService.holdACouncilAction(mb);
                cards = mb.getLocationCards(mb.getLocations());
                dwService.updateDwarvesWhenUpdatedCards(roundDwarvesApprentice, cards);
                break;

            case SPECIAL_CARD_COLLAPSE_THE_SHAFTS:
                locations = mbService.collapseTheShaftsAction(mb);
                cards = mb.getLocationCards(mb.getLocations());
                dwService.updateDwarvesWhenUpdatedCards(roundDwarvesApprentice, cards);
                break;

            case SPECIAL_CARD_RUN_AMOK:
                locations = mbService.runAmokAction(mb);
                cards = mb.getLocationCards(mb.getLocations());
                dwService.updateDwarvesWhenUpdatedCards(roundDwarvesApprentice, cards);
                break;

            case SPECIAL_CARD_SELL_AN_ITEM:
                sellAnItemAction(p, selectedGold, selectedIron, selectedSteal, selectedObject);
                break;

            case SPECIAL_CARD_APPRENTICE:

                g = apprenticeAction(g, p, round, selectedPosition, roundDwarvesApprentice);
                break;

            case SPECIAL_CARD_TURN_BACK:
                locations = mb.getLocations();

                g = turnBackAction(g, p, round, selectedPosition, locations);
                dwService.updateDwarvesWhenUpdatedCards(roundDwarvesApprentice, g.getMainBoard().getCards());
                break;
            case SPECIAL_CARD_PAST_GLORIES:
                locations = mb.getLocations();
                pastGloriesAction(selectedPosition, cardToBeOnTop, locations);

                break;

            default:
                break;
        }
        return g;
    }

    public void initializeSpecialCards() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initializeSpecialCards'");
    }

}
