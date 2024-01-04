package org.springframework.samples.dwarf.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.CardService;
import org.springframework.samples.dwarf.cardDeck.CardDeckService;
import org.springframework.samples.dwarf.dwarf.Dwarf;
import org.springframework.samples.dwarf.location.Location;
import org.springframework.samples.dwarf.location.LocationService;
import org.springframework.samples.dwarf.mainboard.MainBoard;
import org.springframework.samples.dwarf.mainboard.MainBoardService;
import org.springframework.samples.dwarf.object.Object;
import org.springframework.samples.dwarf.player.Player;
import org.springframework.samples.dwarf.player.PlayerRepository;
import org.springframework.samples.dwarf.user.User;
import org.springframework.samples.dwarf.user.UserService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {

    GameRepository gr;
    PlayerRepository pr;
    UserService us;
    MainBoardService mbs;
    CardDeckService cds;
    LocationService ls;
    CardService cs;

    final String helpCard = "HelpCard";
    final String orcCard = "OrcCard";
    final String objectCard = "ObjectCard";
    final String otherCard = "Other";

    @Autowired
    public GameService(GameRepository gr, PlayerRepository pr, UserService us,
            MainBoardService mbs, CardDeckService cds, LocationService ls, CardService cs) {
        this.gr = gr;
        this.pr = pr;
        this.us = us;
        this.mbs = mbs;
        this.cds = cds;
        this.ls = ls;
        this.cs = cs;
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

    @Transactional
    public Optional<List<Player>> getPlayers(Integer id) {
        return gr.getPlayersByGameId(id);
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
    public List<Player> getRemainingTurns(List<Player> plys, List<Dwarf> dwarves, Player starter) {
        ArrayList<Player> remaining_turns = new ArrayList<Player>();

        // Asi se consigue que el que empieza la ronda sea
        // el jugador que debe empezarla
        if (!plys.get(0).equals(starter)) {
            Integer starterIndex = plys.indexOf(starter);
            Collections.rotate(plys, starterIndex);
        }

        remaining_turns.addAll(plys);
        remaining_turns.addAll(plys);
        // Se ponen al final porque deben de ser los ultimos en tirar
        // Es decir, cuando se resuelven las acciones
        for (Dwarf d : dwarves) {
            if (d.getCard() == null) {
                continue;
            }
            if (d.getCard().getCardType().getName().equals("HelpCard")) {
                remaining_turns.add(d.getPlayer());
                remaining_turns.add(d.getPlayer());
            }
        }
        return remaining_turns;
    }

    @Transactional(readOnly = true)
    public Boolean checkRoundNeedsChange(Game g, List<Dwarf> dwarves) {
        Optional<List<Player>> plys_optional = getPlayers(g.getId());
        List<Player> plys = plys_optional.get();

        Integer round = g.getRound();
        List<Dwarf> thisRoundDwarves = dwarves.stream().filter(d -> d.getRound() == round
                && d.getPlayer() != null).toList();

        List<Player> remainingTurns = getRemainingTurns(plys, thisRoundDwarves, g.getPlayerStart());

        return thisRoundDwarves.size() == remainingTurns.size();
    }

    @Transactional
    public Game handleRoundChange(Game g) {
        mbs.faseResolucionAcciones(g);

        MainBoard mb = g.getMainBoard();
        ArrayList<Card> mbCards = new ArrayList<Card>();
        mbCards.addAll(mb.getCards());

        // Card c = g.getMainBoard().getCardDeck().getLastCard();
        // Integer lastCard = g.getMainBoard().getCardDeck().getCards().indexOf(c);
        ArrayList<Card> cd = new ArrayList<Card>();
        // if (lastCard >= g.getMainBoard().getCardDeck().getCards().size() - 2) {
        // Returns an empty list
        // } else {
        try {

            List<Card> twoCards = cds.getNewCards(g.getMainBoard().getCardDeck().getId());
            cd.addAll(twoCards);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }

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

        Optional<List<Player>> plys_optional = getPlayers(g.getId());
        List<Player> plys = plys_optional.get();

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
    public Optional<List<Player>> getPlayersByGameId(Integer gameId) {
        return gr.getPlayersByGameId(gameId);
    }

    @Transactional(readOnly = true)
    public boolean checkPlayerInGameAndGameExists(Game g) {
        boolean check = false;

        if (g == null) {
            return false;
        }

        User currentUser = us.findCurrentUser();
        Optional<List<Player>> l = getPlayersByGameId(g.getId());
        for (Player p : l.get()) {
            if (p.getUser().equals(currentUser)) {
                return true;
            }
        }
        return check;
    }

    @Transactional
    public void changePlayerStart(Game g, ArrayList<Pair<Player, Card>> helpCards) {
        List<Player> players = pr.findAll();

        // Obtemenemos las cartas del juego actual
        ArrayList<Card> currentCards = new ArrayList<>();
        for (Card c : g.getMainBoard().getCards()) {
            // Solo guardamos las cartas que son de orcos
            if (c.getCardType().getName().equals(helpCard)) {
                currentCards.add(c);
            }
        }

        // De esta forma obtenemos las cartas que no han sido seleccionadas
        // al eliminar de las cartas del tablero
        ArrayList<Pair<Player, Card>> selected = new ArrayList<>();
        for (Pair<Player, Card> pc : helpCards) {
            if (currentCards.contains(pc.getSecond())) {
                selected.add(pc);
            }
        }

        // Verificar si el jugador actual (playerStart) seleccionó una carta de tipo
        // "HelpCard"
        for (Pair<Player, Card> pc : selected) {
            if (pc.getFirst().getId().equals(g.getPlayerStart().getId())
                    && pc.getSecond().getCardType().getName().equals(helpCard)) {
                int currentIndex = players.indexOf(pc.getFirst());
                // Asignar el siguiente jugador en la lista como playerStart
                if (currentIndex != -1) {
                    int nextIndex = (currentIndex + 1) % players.size();

                    if (nextIndex < players.size()) {
                        Player nextPlayer = players.get(nextIndex);
                        g.setPlayerStart(nextPlayer);
                        break; // Terminar el bucle después de cambiar el playerStart
                    } else {
                        g.setPlayerStart(g.getPlayerCreator());
                        break; // Terminar el bucle después de cambiar el playerStart al creador del juego
                    }
                } else {
                    // TODO: Crear error
                    System.out.println("Error: No se encontró el jugador actual en la lista.");
                }
            }
        }

        gr.save(g);
    }



    public void handleSpecialCardSelectionDwarvesUsage(SpecialCardRequestHandler request, Player p) {

    }

    @Transactional
    public void resign(Game g, Player p) {
        p.setGold(0);
        p.setIron(0);
        p.setMedal(null);
        p.setObjects(null);
        p.setSteal(0);
    }

    

}