package org.springframework.samples.petclinic.game;

import java.net.URI;
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
import org.springframework.samples.petclinic.cardDeck.CardDeck;
import org.springframework.samples.petclinic.cardDeck.CardDeckService;
import org.springframework.samples.petclinic.dwarf.Dwarf;
import org.springframework.samples.petclinic.dwarf.DwarfService;
import org.springframework.samples.petclinic.exceptions.ResourceNotFoundException;
import org.springframework.samples.petclinic.mainboard.MainBoard;
import org.springframework.samples.petclinic.mainboard.MainBoardService;
import org.springframework.samples.petclinic.player.PlayereService;
import org.springframework.samples.petclinic.specialCardDeck.SpecialCardDeck;
import org.springframework.samples.petclinic.specialCardDeck.SpecialCardDeckService;
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
        } catch(Exception e) {
            System.out.println("Exception =>"+e);
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
    public ResponseEntity<List<Card>> getTwoCards(@PathVariable("code") String code) {
        Game g = gs.getGameByCode(code);

        if (g == null) {
            return ResponseEntity.notFound().build();
        }

        List<Card> cd = cds.getTwoCards(g.getMainBoard().getCardDeck().getId());
        return new ResponseEntity<>(cd, HttpStatus.OK);
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
            ps.savePlayer(p);
        } else {
            System.out.println("This player already in game");
            System.out.println(g);
            System.out.println(u);
            System.out.println(p);
        }

        return ResponseEntity.ok(g);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Game> createGame(@Valid @RequestBody Game g) {
        try {
        ArrayList<String> colours = new ArrayList<String>();
        colours.addAll(List.of("red", "blue", "green", "yellow", "orange", "pink", "purple"));
        Collections.shuffle(colours);

        User u = us.findCurrentUser();

        MainBoard mb = mbs.initialize();
        g.setMainBoard(mb);

        Player p = new Player();
        p.setColor(colours.get(0));
        p.setName(u.getUsername());
        //p.setGame(g);
        p.setUser(u);
        ps.savePlayer(p);

        // Si no se hace asi da error porque
        // al guardarse el game en player, el game todavia no existe
        // y si se asigna a game el player, el player todavia no existe

        g.setPlayerCreator(p);
        gs.saveGame(g);

        p.setGame(g);
        ps.savePlayer(p);
        } catch(Exception e) {
            System.out.println("Exception =>"+e);
            System.out.println("Exception =>"+e.getMessage());
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

    @GetMapping("/play/{code}/dwarves/{round}")
    public ResponseEntity<List<Dwarf>> getDwarvesByRound(@PathVariable("code") String code,
            @PathVariable("round") Integer round) {

        Game g = gs.getGameByCode(code);

        if (g == null) {
            return ResponseEntity.notFound().build();
        }
        List<Dwarf> dwarves = g.getDwarves();
        dwarves = dwarves.stream().filter(d -> d.getRound() == round).toList();
        return new ResponseEntity<>(dwarves, HttpStatus.OK);
    }

    @GetMapping("/play/{code}/players")
    public ResponseEntity<List<Player>> getPlayersFromGame(@PathVariable("code") String code) {

        Game g = gs.getGameByCode(code);
        if (g != null) {
            Optional<List<Player>> plys = gs.getPlayers(g.getId());
            if (plys.isPresent()) {
                return new ResponseEntity<>(plys.get(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping("/play/{code}/dwarves/{user_id}")
    public ResponseEntity<Void> addDwarves(@Valid @RequestBody List<Card> cards, @PathVariable("code") String code,
            @PathVariable("user_id") Integer user_id) {

        Game g = gs.getGameByCode(code);
        if (g == null) {
            return ResponseEntity.notFound().build();
        }

        Dwarf dwarf = new Dwarf();
        dwarf.setPlayer(ps.getPlayerByUserAndGame(us.findUser(user_id), g));
        dwarf.setRound(g.getRound());
        dwarf.setCards(cards);

        ds.saveDwarf(dwarf);

        return new ResponseEntity<>(HttpStatus.OK);

    }

}

