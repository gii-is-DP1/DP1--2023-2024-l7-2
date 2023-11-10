package org.springframework.samples.petclinic.game;

import java.net.URI;
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
    //private final CardDeckService cds;
    //private final SpecialCardDeckService scds;

    @Autowired
    public GameRestController(GameService gs, UserService us, PlayereService ps,
        MainBoardService mbs) {
        this.gs = gs;
        this.us = us;
        this.ps = ps;
        this.mbs = mbs;
        //this.cds = cds;
        //this.scds = scds;
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
        Game g = gs.getGameByCode(code);

        if (g == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/play/{code}")
    public ResponseEntity<Game> playGame(@PathVariable("code") String code) {
        Game g = gs.getGameByCode(code);

        if (g == null) {
            return ResponseEntity.notFound().build();
        }

        return new ResponseEntity<>(g,HttpStatus.OK);
    }

    @GetMapping("/play/{code}/cards")
    public ResponseEntity<List<Card>> getCardsFromMainBoardFromGame(@PathVariable("code") String code) {
        Game g = gs.getGameByCode(code);

        if (g == null) {
            return ResponseEntity.notFound().build();
        }

        return new ResponseEntity<>(g.getMainBoard().getCardDeck().getCards(),HttpStatus.OK);
    }

    @GetMapping("/listPlayers/{id}")
    public ResponseEntity<List<Player>> getPlayers(@PathVariable("id") Integer id) {
        Optional<List<Player>> g = gs.getPlayers(id);

        if (g.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(g.get());
    }

    @PostMapping("/join/{code}/{id}")
    public ResponseEntity<Game> joinGame(@PathVariable("code") String code, @PathVariable("id") Integer id) {
        Game g = gs.getGameByCode(code);
        User u = us.findUser(id);

        if (g == null || u == null) {
            return ResponseEntity.notFound().build();
        }

        Player p = new Player();
        p.setColor("blank");
        p.setName(u.getUsername());
        p.setGame(g);
        p.setUser(u);
        ps.savePlayer(p);

        return ResponseEntity.ok(g);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Game> createGame(@Valid @RequestBody Game g) {

        /* To use this function in the frontend 
         * first the game has to be created and then 
         * the user needs to join the game
        */


        // TODO: REFACTORIZE

        MainBoard mb = mbs.initialize();

        g.setMainBoard(mb);

        gs.saveGame(g);
        
        return new ResponseEntity<>(g,HttpStatus.CREATED);
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
}