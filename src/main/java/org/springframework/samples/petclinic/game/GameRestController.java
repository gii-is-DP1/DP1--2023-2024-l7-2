package org.springframework.samples.petclinic.game;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.cardDeck.CardDeckService;
import org.springframework.samples.petclinic.dwarf.Dwarf;
import org.springframework.samples.petclinic.dwarf.DwarfService;
import org.springframework.samples.petclinic.exceptions.ResourceNotFoundException;
import org.springframework.samples.petclinic.mainboard.MainBoard;
import org.springframework.samples.petclinic.mainboard.MainBoardService;
import org.springframework.samples.petclinic.player.PlayereService;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/game")
@Tag(name = "Games", description = "The games management API")
@SecurityRequirement(name = "bearerAuth")
public class GameRestController {

    private final GameService gs;
    private final UserService us;
    private final PlayereService ps;
    private final MainBoardService mbs;
    private final CardDeckService cds;
    // private final SpecialCardDeckService scds;
    private final DwarfService ds;

    @Autowired
    public GameRestController(GameService gs, UserService us, PlayereService ps,
            MainBoardService mbs, CardDeckService cds, DwarfService ds) {
        this.gs = gs;
        this.us = us;
        this.ps = ps;
        this.mbs = mbs;
        this.cds = cds;
        // this.scds = scds;
        this.ds = ds;
    }

    @GetMapping
    public List<Game> getAllGames(@ParameterObject() @RequestParam(value = "name", required = false) String name,
            @ParameterObject @RequestParam(value = "status", required = false) GameStatus status) {
        if (name != null)
            return gs.getGamesLike(name);
        else if (status != null) {
            switch (status) {
                case WAITING:
                    return gs.getWaitingGames();
                case PLAYING:
                    return gs.getOngoingGames();
                default:
                    return gs.getFinishedGames();
            }
        } else
            return gs.getAllGames();
    }

    @GetMapping("/{id}")
    public Game getGameById(@PathVariable("id") Integer id) {
        Optional<Game> g = gs.getGameById(id);
        if (!g.isPresent())
            throw new ResourceNotFoundException("Game", "id", id);
        return g.get();
    }

    @GetMapping("/check/{code}")
    public ResponseEntity<Void> getGameByCode(@PathVariable("code") String code) {
        Game g = null;
        try {
            g = gs.getGameByCode(code);

            if (g == null) {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.out.println("Exception =>" + e);
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/play/{code}")
    public ResponseEntity<Game> playGame(@PathVariable("code") String code) {
        // comprobar que usuario esta metido en el juego
        Game g = gs.getGameByCode(code);

        if (g == null) {
            return ResponseEntity.notFound().build();
        }

        g.setDwarves(null);

        return new ResponseEntity<>(g, HttpStatus.OK);
    }

    @GetMapping("/play/{code}/cards")
    public ResponseEntity<List<Card>> getCardsFromMainBoardFromGame(@PathVariable("code") String code) {
        Game g = gs.getGameByCode(code);

        if (g == null) {
            return ResponseEntity.notFound().build();
        }

        List<Card> cards = g.getMainBoard().getCardDeck().getCards();

        cards = cards.stream().sorted((c1, c2) -> c1.getPosition().compareTo(c2.getPosition())).toList();

        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    @GetMapping("/play/{code}/getCards")
    public ResponseEntity<List<Card>> getMainBoardCards(@PathVariable("code") String code) {

        Game g = gs.getGameByCode(code);

        if (g == null) {
            return ResponseEntity.notFound().build();
        }
        List<Card> cd = g.getMainBoard().getCards();

        return new ResponseEntity<>(cd, HttpStatus.OK);
        /*
         * // Si se ha acabado el mazo, se caba el juego
         * Card c = g.getMainBoard().getCardDeck().getLastCard();
         * Integer lastCard = g.getMainBoard().getCardDeck().getCards().indexOf(c);
         * if (lastCard >= g.getMainBoard().getCardDeck().getCards().size() - 2) {
         * // Returns an empty list
         * return new ResponseEntity<>(List.of(), HttpStatus.OK);
         * }
         * 
         * List<Card> cd = cds.getTwoCards(g.getMainBoard().getCardDeck().getId());
         */
    }

    @PostMapping("/join/{code}")
    public ResponseEntity<Game> joinGame(@PathVariable("code") String code) {

        Game g = gs.getGameByCode(code);
        User u = us.findCurrentUser();

        // if a player already exists in a game he can just join the game :)
        Player p = ps.getPlayerByUserAndGame(u, g);
        System.out.println(p);
        if (p == null) {
            // We choose a random color
            ArrayList<String> colours = new ArrayList<String>();
            colours.addAll(List.of("red", "blue", "green", "yellow", "orange", "pink", "purple"));
            Collections.shuffle(colours);

            if (g == null || u == null) {
                return ResponseEntity.notFound().build();
            }

            p = new Player();
            p.setColor(colours.get(0));
            p.setName(u.getUsername());
            p.setGame(g);
            p.setUser(u);
            p.setSteal(0);
            p.setGold(0);
            p.setIron(0);
            p.setMedal(0);
            ps.savePlayer(p);
        } else {
            System.out.println("This player already in game");
            System.out.println(g);
            System.out.println(u);
            System.out.println(p);
        }

        return ResponseEntity.ok(g);

    }

    private String getRandomColor(Integer id) {

        ArrayList<String> colours = new ArrayList<String>();
        colours.addAll(List.of("red", "blue", "green", "yellow", "orange", "pink", "purple"));
        Collections.shuffle(colours);

        Optional<List<Player>> players = gs.getPlayers(id);
        if (players.isEmpty()) {
            return colours.get(0);
        }

        players.get().forEach(p -> colours.remove(p.getColor()));
        return colours.get(0);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Game> createGame(@Valid @RequestBody Game g) {
        try {

            User u = us.findCurrentUser();

            MainBoard mb = mbs.initialize();
            g.setMainBoard(mb);

            Player p = new Player();
            p.setColor(getRandomColor(g.getId()));
            p.setName(u.getUsername());
            // p.setGame(g);
            p.setUser(u);
            p.setSteal(0);
            p.setGold(0);
            p.setIron(0);
            p.setMedal(0);
            ps.savePlayer(p);

            // Si no se hace asi da error porque
            // al guardarse el game en player, el game todavia no existe
            // y si se asigna a game el player, el player todavia no existe

            g.setPlayerCreator(p);
            gs.saveGame(g);

            p.setGame(g);
            ps.savePlayer(p);
        } catch (Exception e) {
            System.out.println("Exception =>" + e);
            System.out.println("Exception =>" + e.getMessage());
        }

        g.setMainBoard(null);

        return new ResponseEntity<>(g, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateGame(@Valid @RequestBody Game g, @PathVariable("id") Integer id) {
        Game gToUpdate = getGameById(id);
        BeanUtils.copyProperties(g, gToUpdate, "id");
        gs.saveGame(gToUpdate);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable("id") Integer id) {
        if (getGameById(id) != null)
            gs.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/play/{code}/dwarves")
    public ResponseEntity<List<Dwarf>> getDwarvesByRound(@PathVariable("code") String code) {

        Game g = gs.getGameByCode(code);

        if (g == null) {
            return ResponseEntity.notFound().build();
        }

        Integer round = g.getRound();

        List<Dwarf> dwarves = g.getDwarves();
        // ArrayList<Dwarf> res = new ArrayList<Dwarf>();
        dwarves = dwarves.stream().filter(d -> d.getRound() == round).toList();
        /*
         * dwarves.stream().forEach(d -> {
         * Player p = d.getPlayer();
         * /*
         * if (p == null) {
         * List<Player> players = gs.getPlayers(g.getId()).get();
         * 
         * for (Player tp: players){
         * for (Dwarf td:tp.getDwarfs()) {
         * if (td.getId() == d.getId()) {
         * p = tp;
         * break;
         * }
         * }
         * }
         * }
         * p.setGame(null);
         * //p.setDwarfs(null);
         * d.setPlayer(p);
         * res.add(d);
         * });
         */
        return new ResponseEntity<>(dwarves, HttpStatus.OK);
    }

    @GetMapping("/play/{code}/players")
    public ResponseEntity<List<Player>> getPlayersFromGame(@PathVariable("code") String code) {

        Game g = gs.getGameByCode(code);
        if (g != null) {
            Optional<List<Player>> plys = gs.getPlayers(g.getId());
            if (plys.isPresent()) {

                /*
                 * plys.get().forEach((p) -> {
                 * p.setDwarfs(null);
                 * p.setGame(null);
                 * res.add(p);
                 * });
                 */

                return new ResponseEntity<>(plys.get(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/play/{code}/isMyTurn")
    public ResponseEntity<Boolean> getTurn(@PathVariable("code") String code) {
        // Gets the lists of players and dwarfs. Turn is the next player
        Boolean res = false;

        Game g = gs.getGameByCode(code);
        if (g == null) {
            System.out.println("No game");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<List<Player>> plys_optional = gs.getPlayers(g.getId());
        if (plys_optional.isEmpty()) {
            System.out.println("No players");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Player> plys = plys_optional.get();

        User u = us.findCurrentUser();
        Player p = ps.getPlayerByUserAndGame(u, g);
        if (p == null) {
            System.out.println("no player");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Dwarf> dwarves = g.getDwarves();
        dwarves = dwarves.stream().filter(d -> d.getRound() == g.getRound()).toList();

        List<Player> dwarves_players = dwarves.stream().map(d -> d.getPlayer()).toList();
        System.out.println(dwarves_players);

        ArrayList<Player> players_mutable = new ArrayList<Player>();
        players_mutable.addAll(plys);

        players_mutable.removeAll(dwarves_players);

        if (players_mutable.size() > 0) {
            if (players_mutable.get(0).equals(p)) {
                res = true;
            }
        }

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/play/{code}/isFinished")
    public ResponseEntity<Boolean> endGame(@PathVariable("code") String code) {
        // FALTA CONDICIÓN DE OBJETOS
        Boolean finished = false;
        Game g = gs.getGameByCode(code);

        // tiene que terminar en 6 rondas
        if (g.getRound() >= 2)
            finished = true;

        return new ResponseEntity<>(finished, HttpStatus.OK);
    }

    @PostMapping("/play/{code}/dwarves")
    public ResponseEntity<Void> addDwarves(@Valid @RequestBody List<Card> cards, @PathVariable("code") String code) {

        Game g = gs.getGameByCode(code);
        if (g == null) {
            return ResponseEntity.notFound().build();
        }

        List<Dwarf> dwarves = g.getDwarves();
        dwarves = dwarves.stream().filter(d -> d.getRound() == g.getRound()).toList();

        Optional<List<Player>> plys_optional = gs.getPlayers(g.getId());
        List<Player> plys = plys_optional.get();

        Dwarf dwarf = new Dwarf();
        Player p = ps.getPlayerByUserAndGame(us.findCurrentUser(), g);
        System.out.println(p);

        dwarf.setPlayer(p);
        dwarf.setRound(g.getRound());
        dwarf.setCards(cards);

        System.out.println(cards);

        ds.saveDwarf(dwarf);
        /*
         * ArrayList<Dwarf> dwarflist = new ArrayList<Dwarf>();
         * dwarflist.addAll(p.getDwarfs());
         * dwarflist.add(dwarf);
         * ps.savePlayer(p)
         */

        dwarves = g.getDwarves();
        dwarves.add(dwarf);
        if (dwarves.size() == plys.size()) {
            gs.updateMaterials(g);

            for (Dwarf d : dwarves) {
                d.setPlayer(null);
                d.setCards(null);
                ds.saveDwarf(d);
                ds.deleteDwarf(d);
            }
            dwarves = null;

            MainBoard mb = g.getMainBoard();
            ArrayList<Card> mbCards = new ArrayList<Card>();
            mbCards.addAll(mb.getCards());

            Card c = g.getMainBoard().getCardDeck().getLastCard();
            Integer lastCard = g.getMainBoard().getCardDeck().getCards().indexOf(c);
            ArrayList<Card> cd = new ArrayList<Card>();
            if (lastCard >= g.getMainBoard().getCardDeck().getCards().size() - 2) {
                // Returns an empty list
            } else {
                List<Card> twoCards = cds.getTwoCards(g.getMainBoard().getCardDeck().getId());
                cd.addAll(twoCards);
            }

            for (Card ca : cd) {
                for (int i = 0; i < mbCards.size(); i++) {
                    if (ca.getPosition().equals(mbCards.get(i).getPosition())) {
                        mbCards.set(i, ca);
                    }
                }
            }

            mb.setCards(mbCards);
            mbs.saveMainBoard(mb);

            g.setRound(g.getRound() + 1);
        }
        g.setDwarves(dwarves);

        try {
            gs.saveGame(g);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(e.getMessage());
        }

        System.out.println(g.getDwarves());

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping("/play/{code}/finish")
    public ResponseEntity<Void> finishGameSetWinner(@PathVariable("code") String code) {
        
        Game g = gs.getGameByCode(code);
        if (g == null) {
            return ResponseEntity.notFound().build();
        }

        g.setFinish(LocalDateTime.now());

        Player p = gs.getGameWinner(g);
        System.out.println(p);

        g.setWinner_id(p.getId());

        gs.saveGame(g);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/play/{code}/winner")
    public ResponseEntity<Player> getWinner(@PathVariable("code") String code) {
        Game g = gs.getGameByCode(code);
        if (g == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ps.getById(g.getWinner_id()), HttpStatus.OK);
    }

}
