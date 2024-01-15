package org.springframework.samples.dwarf.mainboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.CardService;
import org.springframework.samples.dwarf.card.SpecialCard;
import org.springframework.samples.dwarf.card.SpecialCardRepository;
import org.springframework.samples.dwarf.cardDeck.CardDeck;
import org.springframework.samples.dwarf.cardDeck.CardDeckService;
import org.springframework.samples.dwarf.dwarf.Dwarf;
import org.springframework.samples.dwarf.game.Game;
import org.springframework.samples.dwarf.location.Location;
import org.springframework.samples.dwarf.location.LocationService;
import org.springframework.samples.dwarf.player.Player;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class MainBoardService {

    final String helpCard = "HelpCard";
    final String orcCard = "OrcCard";
    final String objectCard = "ObjectCard";
    final String otherCard = "Other";

    private final MainBoardRepository repo;
    private final SpecialCardRepository specCardRepo;
    private final CardDeckService cds;
    private final CardService cs;
    private final LocationService ls;

    @Autowired
    public MainBoardService(MainBoardRepository repo, SpecialCardRepository specCardRepo, CardDeckService cds, 
        CardService cs, LocationService ls) {
        this.repo = repo;
        this.cds = cds;
        this.cs = cs;
        this.ls = ls;
        this.specCardRepo = specCardRepo;
    }

    @Transactional(readOnly = true)
    public List<MainBoard> getMainBoard() {
        return repo.findAll();
    }

    @Transactional
    public MainBoard saveMainBoard(@Valid MainBoard newMainBoard) {
        return repo.save(newMainBoard);
    }

    @Transactional(readOnly = true)
    public MainBoard getById(int id) {
        Optional<MainBoard> result = repo.findById(id);
        return result.isPresent() ? result.get() : null;
    }

    @Transactional(readOnly = true)
    public List<SpecialCard> initializeSpecialCards() {
        ArrayList<SpecialCard> cards = new ArrayList<SpecialCard>();
        cards.addAll(specCardRepo.findAll());

        Collections.shuffle(cards);
        return cards;
    }

    @Transactional
    public MainBoard initialize() {

        CardDeck cardDecks = cds.initialiate();
        //SpecialCardDeck specCardDeck = scds.initialize();

        MainBoard mb = new MainBoard();
        mb.setCardDeck(cardDecks);
        //mb.setSpecialCardDeck(specCardDeck);

        List<Location> locations = ls.initialize();
        mb.setLocations(locations);

        List<SpecialCard> specCards = initializeSpecialCards();
        mb.setSCards(specCards);
        
        mb = saveMainBoard(mb);

        return mb;
    }

    @Transactional(readOnly = true)
    public List<SpecialCard> getSpecialCards(MainBoard mb) {
        List<SpecialCard> specCards = mb.getSCards();
        List<SpecialCard> res = specCards.subList(0, 3);
        return res;
    }

    @Transactional
    public void holdACouncilAction(MainBoard mb) {
        CardDeck cd = mb.getCardDeck();
        List<Card> cardsRemovedFromLocations = new ArrayList<>();

        for (Location lc:mb.getLocations()) {
            Card removedCard = ls.removeLastCard(lc);
            cardsRemovedFromLocations.add(removedCard);
        }

        cardsRemovedFromLocations.addAll(cd.getCards());

        cd = cds.shuffleAndSaveCards(cd, cardsRemovedFromLocations);

        // mb = saveMainBoard(mb);
        //return mb;
    }

    @Transactional
    public List<Location> collapseTheShaftsAction(MainBoard mb) {

        List<Location> locations = new ArrayList<>();
        for (Location lc:mb.getLocations()) {
            Location l = ls.putFirstCardAtEnd(lc);
            locations.add(l);
        }
        //mb.setLocations(locations);
        //mb = saveMainBoard(mb);
        return locations;
    }

    @Transactional
    public List<Location> runAmokAction(MainBoard mb) {
        List<Location> newLocations = new ArrayList<>();
        for (Location lc:mb.getLocations()) {
            Location newLocation = ls.shuffleLocation(lc);
            newLocations.add(newLocation);
        }
        //mb.setLocations(newLocations);
        //mb = saveMainBoard(mb);
        return newLocations;
    }
    

    @Transactional
    public void updateMaterials(ArrayList<Pair<Player, Card>> cards) {
        for (Pair<Player, Card> pc : cards) {
            Player p = pc.getFirst();
            Card c = pc.getSecond();
            cs.updateMaterialsSingleAction(p, c);
        }
    }

    /*
    @Transactional
    public void adwardMedal(ArrayList<Pair<Player, Card>> orcCards) {

        for (Pair<Player, Card> pc : orcCards) {
            Player p = pc.getFirst();
            Card c = pc.getSecond();

            cs.adwardMedalSingleAction(p, c);
        }
    }*/

    @Transactional
    public void faseForjar(ArrayList<Pair<Player, Card>> playerCards) {

        for (Pair<Player, Card> pc : playerCards) {
            Player p = pc.getFirst();
            Card c = pc.getSecond();
            cs.forjarSingleAction(p, c);
        }

    }

    @Transactional
    private ArrayList<Pair<Player, Card>> getChoosedCards(List<Dwarf> dwarves) {
        ArrayList<Pair<Player, Card>> cards = new ArrayList<Pair<Player, Card>>();
        for (Dwarf d : dwarves) {
            if (d.getCard() == null) {
                continue;
            }
            
            Pair<Player, Card> playerPair;
            if (d.getPlayer() == null) {
                 playerPair = Pair.of(new Player(), d.getCard());
            } else {
                playerPair = Pair.of(d.getPlayer(), d.getCard());
            }
            cards.add(playerPair);
        }
        return cards;
    }

    @Transactional
    private HashMap<String, ArrayList<Pair<Player, Card>>> splitCardsByType(ArrayList<Pair<Player, Card>> cards) {
        HashMap<String, ArrayList<Pair<Player, Card>>> cardsByType = new HashMap<String, ArrayList<Pair<Player, Card>>>();
        for (Pair<Player, Card> c : cards) {
            String type = c.getSecond().getCardType().getName();
            if (cardsByType.containsKey(type)) {
                cardsByType.get(type).add(c);
            } else {
                ArrayList<Pair<Player, Card>> cardList = new ArrayList<Pair<Player, Card>>();
                cardList.add(c);
                cardsByType.put(type, cardList);
            }
        }
        return cardsByType;
    }

    @Transactional
    public ArrayList<Pair<Player, Card>> faseResolucionAcciones(Game g) {
        /*
         * 1. Recibir ayuda
         * 2. Defenter
         * 3. Extraear mineral
         * 4. Forjar
         */
        List<Dwarf> dwarves = g.getDwarves();
        dwarves = dwarves.stream().filter(d -> d.getRound() == g.getRound() && d.getCard() != null).toList();

        //List<Player> plys = g.getPlayers();

        // Obtenemos todas las cartas y las guardamos
        // con su player para despues aplicarselo al player
        ArrayList<Pair<Player, Card>> cards = getChoosedCards(dwarves);

        // Separamos las cartas por su tipo para
        // poder aplicar cada tipo en el ordern correcto
        HashMap<String, ArrayList<Pair<Player, Card>>> cardsByType = splitCardsByType(cards);

        // Obtenemos las cartas por su tipo
        ArrayList<Pair<Player, Card>> helpCards = cardsByType.get(helpCard);
        ArrayList<Pair<Player, Card>> orcCards = cardsByType.get(orcCard);
        ArrayList<Pair<Player, Card>> objectCards = cardsByType.get(objectCard);
        ArrayList<Pair<Player, Card>> normalCards = cardsByType.get(otherCard);

        // Las cartas de la fase anterior pueden hacer que no se haga la siguiente fase
        Boolean canContinue = true;

        // Acciones de las cartas de orcos
        if (canContinue) {
            canContinue = faseOrcos(g, orcCards);
        } else {
            canContinue = true;
        }

        // Fase de recoleccion
        if (canContinue && normalCards != null) {
            updateMaterials(normalCards);
        } 

        if (canContinue && objectCards != null) {
            faseForjar(objectCards);
        }

        return helpCards;
    }

    @Transactional
    public Boolean faseOrcos(Game g, ArrayList<Pair<Player, Card>> orcCards) {
        Boolean res = true;

        // Obtemenemos las cartas del juego actual
        ArrayList<Card> currentCards = new ArrayList<>();
        for (Card c : g.getMainBoard().getCards()) {
            // Solo guardamos las cartas que son de orcos
            if (c.getCardType().getName().equals(orcCard)) {
                currentCards.add(c);
            }
        }

        // Si no hay ninguna carta de orcos salimos de la funcion
        if (currentCards.size() == 0)
            return res;

        if (orcCards != null) {
            for (Pair<Player, Card> pc : orcCards) {
                if (currentCards.contains(pc.getSecond())) {
                    currentCards.remove(pc.getSecond());

                    Player p = pc.getFirst();
                    Card c = pc.getSecond();
                    if (p.getUser() != null) {
                        cs.adwardMedalSingleAction(p,c);
                    }
                }
            }
        }

        List<Player> players = g.getPlayers();
        if (currentCards.size() > 0) {
            for (Card pc : currentCards) {
                switch (pc.getName()) {
                    case "Orc Raiders":
                        // Si se selecciona esta carta no se hace la fase de recolección
                        res = false;
                        break;
                    case "Dragon":
                        cs.orcCardDragonAction(players);
                        break;
                    case "Sidhe":
                        cs.orcCardSidheAction(players);
                        break;
                    case "Knockers":
                        cs.orcCardKnockersAction(players);
                        break;
                    case "Great Dragon":
                        cs.orcCardGreatDragonAction(players);
                        break;
                }
            }
        }
        // el return es si se efectua la siguiente fase o no
        return res;

    }

    @Transactional
    public void applySingleCardWhenSpecialCard(List<Dwarf> roundDwarves, Card reverseCard) {
        for (Dwarf d : roundDwarves) {
            Card dwarfCard = d.getCard();
            Player p = d.getPlayer();

            if (reverseCard.getPosition().equals(dwarfCard.getPosition())) {
                applySingleCardWhenSpecialCardAction(p, dwarfCard);
            }
        }
    }

    @Transactional
    public void applySingleCardWhenSpecialCardAction(Player p, Card c) {
        switch (c.getCardType().getName()) {
            case otherCard:
                cs.updateMaterialsSingleAction(p, c);
                break;
            case objectCard:
                cs.forjarSingleAction(p,c);
                break;
            case orcCard:
                cs.adwardMedalSingleAction(p,c);
                break;
            default:
                break;
        }
    }

    @Transactional
    public MainBoard removeUsedSpecialCard(MainBoard mb, SpecialCard sc) {
        List<SpecialCard> newSpecCards = mb.getSCards();
        
        for( int i = 0 ; i < newSpecCards.size() ; i++) {
            SpecialCard tmp = newSpecCards.get(i);
            if (tmp.getName().equals(sc.getName())) {
                newSpecCards.remove(i);
                break;
            }
        }
        
        mb.setSCards(newSpecCards);
        //mb = saveMainBoard(mb);
        return mb;
    }

    @Transactional
    public MainBoard putReverseSpecialCard(MainBoard mb, Card reverseCard) {
        //MainBoard mb = g.getMainBoard();
        List<Location> newLocations = mb.getLocations();
        Location locationToUpdate = newLocations.get(reverseCard.getPosition() - 1);
        locationToUpdate = ls.pushCard(locationToUpdate, reverseCard);
        newLocations.set(reverseCard.getPosition() - 1, locationToUpdate);
        mb.setLocations(newLocations);
        //mb = saveMainBoard(mb);

        return mb;
    }

    @Transactional
    public MainBoard handleSpecialCardTurn(MainBoard mb, Card reverseCard, SpecialCard specialCard, List<Dwarf> roundDwarves) {
        applySingleCardWhenSpecialCard(roundDwarves, reverseCard);


        mb = removeUsedSpecialCard(mb, specialCard);
        mb = putReverseSpecialCard(mb, reverseCard);
        mb = saveMainBoard(mb);
        return mb;
    }
}
