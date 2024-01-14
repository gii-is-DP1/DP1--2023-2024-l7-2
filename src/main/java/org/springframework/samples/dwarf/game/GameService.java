package org.springframework.samples.dwarf.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

import org.springframework.samples.dwarf.object.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.CardService;
import org.springframework.samples.dwarf.card.CardType;
import org.springframework.samples.dwarf.cardDeck.CardDeckService;
import org.springframework.samples.dwarf.chat.Chat;
import org.springframework.samples.dwarf.chat.ChatService;
import org.springframework.samples.dwarf.dwarf.Dwarf;
import org.springframework.samples.dwarf.dwarf.DwarfService;
import org.springframework.samples.dwarf.location.Location;
import org.springframework.samples.dwarf.location.LocationService;
import org.springframework.samples.dwarf.mainboard.MainBoard;
import org.springframework.samples.dwarf.mainboard.MainBoardService;
import org.springframework.samples.dwarf.player.Player;
import org.springframework.samples.dwarf.player.PlayerRepository;
import org.springframework.samples.dwarf.player.PlayerService;
import org.springframework.samples.dwarf.spectator.Spectator;
import org.springframework.samples.dwarf.user.User;
import org.springframework.samples.dwarf.user.UserService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {

    GameRepository gr;
    PlayerService ps;
    DwarfService ds;
    UserService us;
    MainBoardService mbs;
    CardDeckService cds;
    LocationService ls;
    CardService cs;
    ChatService chatservice;


    private final Integer OBJECTS_NEEDED_TO_END = 4;
    private final Integer MAX_ROUNDS = 6;
    private final Integer MAX_DWARVES_PER_PLAYER = 4;
    final String helpCard = "HelpCard";
    final String orcCard = "OrcCard";
    final String objectCard = "ObjectCard";
    final String otherCard = "Other";

    @Autowired
    public GameService(GameRepository gr, PlayerService ps, UserService us,
            MainBoardService mbs, CardDeckService cds, LocationService ls, CardService cs, 
            DwarfService ds,
            ChatService chatService) {
        this.gr = gr;
        this.ps = ps;
        this.us = us;
        this.mbs = mbs;
        this.cds = cds;
        this.ls = ls;
        this.cs = cs;
        this.chatservice = chatService;
    }

    @Transactional(readOnly = true)
    public List<Game> getAllGames() {
        return gr.findAll();
    }

    @Transactional(readOnly = true)
    public List<Game> getWaitingGames() {
        return gr.findByStart(null);
    }

    @Transactional
    public Game saveGame(Game g) {
        return gr.save(g);
    }

    @Transactional
    public Optional<Game> getGameById(Integer id) {
        return gr.findById(id);
    }

    @Transactional(readOnly = true)
    public Game getGameByCode(String code) {
        List<Game> games = gr.findByCode(code);
        return games.isEmpty() ? null : games.get(0);
    }

    @Transactional(readOnly = true)
    public List<Game> getGamesLike(String pattern) {
        return gr.findByNameContains(pattern);
    }

    @Transactional(readOnly = true)
    public List<Game> getFinishedGames() {
        return gr.findByFinishIsNotNull();
    }

    @Transactional(readOnly = true)
    public List<Game> getOngoingGames() {
        return gr.findByFinishIsNullAndStartIsNotNull();
    }

    @Transactional
    public void delete(Integer id) {
        Game toDelete = getGameById(id).get();
        this.gr.delete(toDelete);
    }

    @Transactional(readOnly = true)
    public List<Game> getAllPublicGames() {
        return gr.findAllPublicGames();
    }

    @Transactional(readOnly = true)
    public Page<Game> getAllPublicGames(Pageable pageable) {
        return gr.findAllPublicGames(pageable);
    }

    @Transactional(readOnly = true)
    public List<Dwarf> getRoundDwarfs(Game g, Integer round) {
        List<Dwarf> roundDwarves = g.getDwarves();
        roundDwarves = roundDwarves.stream()
                .filter(d -> d.getRound() == round && d.getPlayer() != null && d.getCard() != null).toList();
        return roundDwarves;
    }

    @Transactional
    public Game initalize(Game g, User u) {
        MainBoard mb = mbs.initialize();
        g.setMainBoard(mb);

        Player p = ps.initialize(u.getUsername());
        p.setColor(ps.getRandomColor(List.of()));
        // p.setGame(g);
        p.setUser(u);
        p.setObjects(new ArrayList<Object>());
        p = ps.savePlayer(p);

        // Si no se hace asi da error porque
        // al guardarse el game en player, el game todavia no existe
        // y si se asigna a game el player, el player todavia no existe

        g.setPlayerCreator(p);
        g.setPlayerStart(p);
        g.setPlayers(List.of(p));

        Chat chat = chatservice.initialize();
        g.setChat(chat);

        g = saveGame(g);
        return g;
    }

    @Transactional(readOnly = true)
    public Boolean needsToEnd(Game g){
        List<Player> plys = g.getPlayers();
        Boolean finished = false;
        List<Card> cardDeckRemainingCards = g.getMainBoard().getCardDeck().getCards();
        if (cardDeckRemainingCards.size() == 0) {
            finished = true;
        }

        // tiene que terminar si un jugador consigue 4 objetos
        if (!finished) {
            for (Player p : plys) {
                if (p.getObjects() != null && p.getObjects().size() >= OBJECTS_NEEDED_TO_END) {
                    finished = true;
                    break;
                }
            }
        }

        // tiene que terminar en 6 rondas
        if (finished || g.getRound() >= MAX_ROUNDS)
            finished = true;

        if (finished || g.getFinish() != null)
            finished = true;
        return finished;
    }

    @Transactional
    public void joinPlayer(Game g, User u) {
        Player p = ps.initialize(u.getUsername());
        p.setColor(ps.getRandomColor(g.getPlayers()));
        p.setUser(u);
        p.setObjects(new ArrayList<Object>());

        ps.savePlayer(p);

        if (g.getPlayerStart() == null) {
            g.setPlayerStart(p);
        }

        addPlayer(g, p);
    }

    @Transactional
    public Game addPlayer(Game g, Player p) {
        List<Player> players = g.getPlayers();
        players.add(p);
        g.setPlayers(players);
        return saveGame(g);
    }

    @Transactional
    public Game addSpectator(Game g, Spectator s) {
        List<Spectator> spectators = g.getSpectators();
        spectators.add(s);
        g.setSpectators(spectators);
        return saveGame(g);
    }

    @Transactional
    public Game addDwarf(Game g, Player p , Card card) {
        Dwarf dwarf = new Dwarf();
        

        dwarf.setPlayer(p);
        dwarf.setRound(g.getRound());
        dwarf.setCard(card);

        ds.saveDwarf(dwarf);

        List<Dwarf> dwarves = g.getDwarves();
        dwarves.add(dwarf);
        g.setDwarves(dwarves);

        g = saveGame(g);
        return g;
    }

    @Transactional(readOnly = true)
    public Boolean gameContainsPlayer(Game g, User u) {
        Boolean res = false;

        for (Player p : g.getPlayers()) {
            if (p.getUser().equals(u)) {
                res = true;
                break;
            }
        }
        return res;
    }

    @Transactional(readOnly = true)
    public Boolean gameContainsSpectator(Game g, User u) {
        Boolean res = false;

        for (Spectator s : g.getSpectators()) {
            if (s.getUser().equals(u)) {
                res = true;
                break;
            }
        }
        return res;
    }

    @Transactional(readOnly = true)
    public List<Player> getRemainingTurns(List<Player> tmp_plys, List<Dwarf> dwarves, Player starter) {
        ArrayList<Player> remaining_turns = new ArrayList<Player>();

        ArrayList<Player> plys = new ArrayList<Player>();
        for (Player p: tmp_plys) {
            plys.add(p);
        }

        // Asi se consigue que el que empieza la ronda sea
        // el jugador que debe empezarla
        if (!plys.get(0).equals(starter)) {
            Integer starterIndex = plys.indexOf(starter);
            Collections.rotate(plys, -starterIndex);
        }



        ArrayList<Player> turnsOfHelpCard = new ArrayList<Player>();
        ArrayList<Player> turnsOfSpecialCard = new ArrayList<Player>();
        ArrayList<Player> extraDwarfWhenSpecialCard = new ArrayList<Player>();
        // Se ponen al final porque deben de ser los ultimos en tirar
        // Es decir, cuando se resuelven las acciones
        Map<Player,Boolean> hasUsedSpecialCard = new HashMap<Player,Boolean>();
        Player lastPlayer = null;
        Boolean usedSingleDwarfForSpecialCard = false;
        for (Dwarf d : dwarves) {

            Player p = d.getPlayer();
            if (p == null) continue;
            
            Card c = d.getCard();
            if (!hasUsedSpecialCard.containsKey(p)) {
                hasUsedSpecialCard.put(p, false);
            }

            // Se ha usado una carta especial
            if (c == null){ 

                if (!hasUsedSpecialCard.get(p)) {
                    hasUsedSpecialCard.put(p,true);
                }

                if (lastPlayer != null && lastPlayer.getName().equals(p.getName())) {
                    // No se ha cambiado el turno, lo
                    // que significa que el jugador ha
                    // usado una carta especial con los dos dwarves

                    //hasUsedSpecialCard.put(p, false);
                    usedSingleDwarfForSpecialCard = false;

                } else {

                    if (usedSingleDwarfForSpecialCard) {
                        // Como el jugador ha usado solo un dwarf, el que le queda 
                        // se usa al final de la ronda
                        turnsOfSpecialCard.add(lastPlayer);
                        plys.remove(lastPlayer);
                    } else {
                        usedSingleDwarfForSpecialCard = true;
                    }

                    if (lastPlayer != null && hasUsedSpecialCard.get(lastPlayer)) {
                        hasUsedSpecialCard.put(lastPlayer,false);
                    }
                }
                
            } else { // No se ha usado una carta especial

                if (hasUsedSpecialCard.get(p)) {

                    if(lastPlayer != null && lastPlayer.getName().equals(p.getName())) {
                        extraDwarfWhenSpecialCard.add(p); // Algunas cartas especiales te dan un dwarf de mas

                        if(usedSingleDwarfForSpecialCard) {
                            turnsOfSpecialCard.add(lastPlayer);
                            plys.remove(lastPlayer);
                            usedSingleDwarfForSpecialCard = false;                            
                        }

                    } else if (usedSingleDwarfForSpecialCard) {
                        turnsOfSpecialCard.add(lastPlayer);
                        plys.remove(lastPlayer);
                        usedSingleDwarfForSpecialCard = false;
                    }
                    hasUsedSpecialCard.put(p,false);
                } 
                
                if (lastPlayer != null && hasUsedSpecialCard.get(lastPlayer)) {
                    if (usedSingleDwarfForSpecialCard) {
                        turnsOfSpecialCard.add(lastPlayer);
                        plys.remove(lastPlayer);
                        usedSingleDwarfForSpecialCard = false;
                    }
                    hasUsedSpecialCard.put(lastPlayer,false);
                }
                
    
                CardType ct = c.getCardType();
                if (ct.getName().equals(helpCard)) {
                    turnsOfHelpCard.add(p);
                    turnsOfHelpCard.add(p);
                }
            }
            lastPlayer = p;

        }

        // Primero los jugadores normales que no han usado cartas especiales
        remaining_turns.addAll(plys);
        remaining_turns.addAll(plys);

        // Los turnos especiales por el uso de una carta especial
        if (extraDwarfWhenSpecialCard.size() > 0) {
            remaining_turns.addAll(extraDwarfWhenSpecialCard);
        }

        // Despues los turnos por la carta de ayuda
        if (turnsOfHelpCard.size()>0) {
            remaining_turns.addAll(turnsOfHelpCard);
        }

        //Invertimos los turnos de los jugadores que han seleccionado una carta especial 
        // con solo un dwarf y lo addeamos
        if (turnsOfSpecialCard.size() > 0){
            Collections.reverse(turnsOfSpecialCard);
            remaining_turns.addAll(turnsOfSpecialCard);
            remaining_turns.addAll(turnsOfSpecialCard);
        }

        return remaining_turns;
    }

    @Transactional(readOnly = true)
    public Boolean checkRoundNeedsChange(Game g, List<Dwarf> dwarves) {
        List<Player> plys = g.getPlayers();

        Integer round = g.getRound();
        List<Dwarf> thisRoundDwarves = dwarves.stream().filter(d -> d.getRound() == round
                && d.getPlayer() != null).toList();

        List<Player> remainingTurns = getRemainingTurns(plys, thisRoundDwarves, g.getPlayerStart());

        return thisRoundDwarves.size() >= remainingTurns.size() 
            || thisRoundDwarves.size() >= MAX_DWARVES_PER_PLAYER*plys.size() ;
    }

    @Transactional
    public Player getCurrentTurnPlayer(List<Player> plys, List<Dwarf> dwarves, Player playerStarter) {
        Player res = null;

        List<Player> dwarves_players = dwarves.stream().map(d -> d.getPlayer()).toList();

        ArrayList<Player> remaining_turns = new ArrayList<Player>();
        remaining_turns.addAll(getRemainingTurns(plys, dwarves, playerStarter));

        // Partimos de la premisa de que cada ronda se componen de 2 turnos por cada
        // jugador
        for (Player p_dwarf : dwarves_players) {
            // ArrayList<Player> tmp_remaining_turns = new ArrayList<>(remaining_turns);
            for (int i = 0; i < remaining_turns.size(); i++) {
                if (remaining_turns.get(i).equals(p_dwarf)) {
                    remaining_turns.remove(i);
                    break;
                }
            }
        }

        if (remaining_turns.size() > 0) {
            res = remaining_turns.get(0);
        }

        return res;
    }


    @Transactional
    public Game handleRoundChange(Game g) {
        ArrayList<Pair<Player, Card>> helpCards = helpCards = mbs.faseResolucionAcciones(g);

        if (helpCards != null) {
            g = changePlayerStart(g, helpCards);
        }

        MainBoard mb = g.getMainBoard();
        ArrayList<Card> mbCards = new ArrayList<Card>();
        mbCards.addAll(mb.getCards());

        ArrayList<Card> cd = new ArrayList<Card>();

        List<Card> twoCards = cds.getNewCards(g.getMainBoard().getCardDeck().getId());
        cd.addAll(twoCards);

        List<Location> locations = mb.getLocations();
        for (Card ca : cd) {
            Integer position = ca.getPosition();
            ls.pushCard(locations.get(position - 1), ca);
        }

        g.setRound(g.getRound() + 1);

        return g;
    }

    @Transactional(readOnly = true)
    public Player getGameWinner(Game g) {

        List<Player> plys = g.getPlayers();

        Map<Player, Integer> totalScore = new HashMap<Player, Integer>();
        plys.stream().forEach(p -> totalScore.put(p, 0));

        Player winner = null;

        Integer maxSteal = 0;
        // ArrayList<Player> pWithMoreSteal = new ArrayList<>();
        Integer maxGold = 0;
        // ArrayList<Player> pWithMoreGold = new ArrayList<>();
        Integer maxObjects = 0;

        // Calculamos el maximo de cada material
        for (Player p : plys) {
            if (p.getSteal() > maxSteal) {
                maxSteal = p.getSteal();
            }
            if (p.getGold() > maxGold) {
                maxGold = p.getGold();
            }
            if (p.getObjects().size() > maxObjects) {
                maxObjects = p.getObjects().size();
            }
        }

        // Calculamos las mayorias que tiene cada jugador
        for (Player p : plys) {
            if (maxSteal > 0 && p.getSteal() == maxSteal) {
                totalScore.put(p, totalScore.get(p) + 1);
            }
            if (maxGold > 0 && p.getGold() == maxGold) {
                totalScore.put(p, totalScore.get(p) + 1);
            }
            if (maxObjects > 0 && p.getObjects().size() == maxObjects) {
                totalScore.put(p, totalScore.get(p) + 1);
            }
        }

        // Obtenemos el maximo numero de mayorias
        Integer maxMayorias = 0;
        ArrayList<Player> pWithMoreMayorias = new ArrayList<>();
        for (Integer puntuacion : totalScore.values()) {
            if (puntuacion > maxMayorias) {
                maxMayorias = puntuacion;
            }
        }

        for (Entry<Player, Integer> e : totalScore.entrySet()) {
            if (e.getValue() == maxMayorias) {
                pWithMoreMayorias.add(e.getKey());
            }
        }

        if (pWithMoreMayorias.size() > 1) {
            Integer maxMedal = 0;
            ArrayList<Player> pWithMoreMedals = new ArrayList<>();
            for (Player p : pWithMoreMayorias) {
                if (p.getMedal() > maxMedal) {
                    maxMedal = p.getMedal();
                }
            }

            for (Player p : pWithMoreMayorias) {
                if (p.getMedal() == maxMedal) {
                    pWithMoreMedals.add(p);
                }
            }

            // Si hay mas de un jugador con mayoria en medallas
            // hay que desempatar en hierro
            if (pWithMoreMedals.size() > 1) {
                Integer maxIron = 0;
                ArrayList<Player> pWithMoreIron = new ArrayList<>();
                for (Player p : pWithMoreMedals) {
                    if (p.getIron() > maxIron) {
                        maxIron = p.getIron();
                    }
                }

                for (Player p : pWithMoreMedals) {
                    if (p.getIron() == maxIron) {
                        pWithMoreIron.add(p);
                    }
                }

                // Si hay mas de un jugador con mayoria en hierro
                // hay que desempatar en objetos
                if (pWithMoreIron.size() > 1) {

                    for (Player p : pWithMoreIron) {
                        // maxObjects was calculated at the begining of this process
                        if (p.getIron() == maxObjects) {
                            winner = p;
                            break;
                        }
                    }
                } else {
                    winner = pWithMoreIron.get(0);
                }

            } else {
                winner = pWithMoreMedals.get(0);
            }

        } else {
            winner = pWithMoreMayorias.get(0);
        }

        return winner;

    }

    @Transactional(readOnly = true)
    public Player getPlayerByUserAndGame(User u, Game g) {
        Player res = null;
        for (Player p : g.getPlayers()) {
            if (p.getUser().equals(u)) {
                res = p;
                break;
            }
        }
        return res;
    }

    @Transactional(readOnly = true)
    public boolean checkPlayerInGameAndGameExists(Game g) {
        boolean check = false;

        if (g == null) {
            return false;
        }

        User currentUser = us.findCurrentUser();
        if (gameContainsPlayer(g, currentUser)) {
            check = true;
        }
        return check;
    }

    @Transactional
    public Game changePlayerStart(Game g, ArrayList<Pair<Player, Card>> helpCards) {
        // ArrayList<Player> players = new ArrayList<>();
        // players.addAll(g.getPlayers());
        List<Player> players = g.getPlayers();

        Integer playerStarterId = g.getPlayerStart().getId();
        for (Pair<Player, Card> pc : helpCards) {
            Player p = pc.getFirst();
            if (p.getId().equals(playerStarterId)) {
                Integer position = players.indexOf(p);
                Player newStarter = players.get((position + 1) % players.size());
                g.setPlayerStart(newStarter);

                break;
            }
        }
        g = saveGame(g);

        return g;
    }

    /* 
    public void handleSpecialCardSelectionDwarvesUsage(SpecialCardRequestHandler request, Player p) {

    }*/

    @Transactional
    public void resign(Game g, Player p) {
        p.setGold(0);
        p.setIron(0);
        p.setMedal(null);
        p.setObjects(null);
        p.setSteal(0);
    }

}