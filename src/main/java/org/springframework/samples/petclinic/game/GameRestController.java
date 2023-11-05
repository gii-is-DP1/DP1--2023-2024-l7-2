package org.springframework.samples.petclinic.game;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.exceptions.ResourceNotFoundException;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/game")
@Tag(name = "Games", description = "The games management API")
@SecurityRequirement(name = "bearerAuth")
public class GameRestController {

    private final GameService gs;
    private final UserService us;
    private final PlayereService ps;

    @Autowired
    public GameRestController(GameService gs, UserService us, PlayereService ps) {
        this.gs = gs;
        this.us = us;
        this.ps = ps;
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
        p.setName(u.getUsername());
        p.setGame(g);
        ps.savePlayer(p);

        return ResponseEntity.ok(g);

    }

    @PostMapping
    public ResponseEntity<Game> createGame(@Valid @RequestBody Game g) {
        g = gs.saveGame(g);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(g.getId())
                .toUri();
        return ResponseEntity.created(location).body(g);
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