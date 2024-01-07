package org.springframework.samples.dwarf.game;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.SpecialCard;
import org.springframework.samples.dwarf.cardDeck.CardDeckService;
import org.springframework.samples.dwarf.chat.Chat;
import org.springframework.samples.dwarf.chat.ChatService;
import org.springframework.samples.dwarf.dwarf.Dwarf;
import org.springframework.samples.dwarf.dwarf.DwarfService;
import org.springframework.samples.dwarf.exceptions.ResourceNotFoundException;
import org.springframework.samples.dwarf.location.Location;
import org.springframework.samples.dwarf.location.LocationService;
import org.springframework.samples.dwarf.mainboard.MainBoard;
import org.springframework.samples.dwarf.mainboard.MainBoardService;
import org.springframework.samples.dwarf.object.Object;
import org.springframework.samples.dwarf.player.Player;
import org.springframework.samples.dwarf.player.PlayerService;
import org.springframework.samples.dwarf.specialCardDeck.SpecialCardDeckService;
import org.springframework.samples.dwarf.spectator.Spectator;
import org.springframework.samples.dwarf.spectator.SpectatorService;
import org.springframework.samples.dwarf.user.User;
import org.springframework.samples.dwarf.user.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
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
    private final PlayerService ps;
    private final MainBoardService mbs;
    private final CardDeckService cds;
    private final SpecialCardDeckService scds;
    private final LocationService ls;
    private final DwarfService ds;
    private final SpectatorService specservice;
    private final ChatService chatservice;

    @Autowired
    public GameRestController(GameService gs, UserService us, PlayerService ps,
            MainBoardService mbs, CardDeckService cds, DwarfService ds,
            SpecialCardDeckService scds, LocationService ls, SpectatorService specservice, ChatService chatservice) {
        this.gs = gs;
        this.us = us;
        this.ps = ps;
        this.mbs = mbs;
        this.cds = cds;
        this.scds = scds;
        this.ds = ds;
        this.ls = ls;
        this.specservice = specservice;
        this.chatservice = chatservice;
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

        if (!gs.checkPlayerInGameAndGameExists(g)) {
            return ResponseEntity.notFound().build();
        }

        List<Card> cards = g.getMainBoard().getCardDeck().getCards();

        cards = cards.stream().sorted((c1, c2) -> c1.getPosition().compareTo(c2.getPosition())).toList();

        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    @GetMapping("/play/{code}/getCards")
    public ResponseEntity<List<Card>> getMainBoardCards(@PathVariable("code") String code) {

        Game g = gs.getGameByCode(code);
        if (!gs.checkPlayerInGameAndGameExists(g)) {
            return ResponseEntity.notFound().build();
        }

        List<Card> cd = g.getMainBoard().getCards();

        return new ResponseEntity<>(cd, HttpStatus.OK);
    }

    @GetMapping("/play/{code}/getSpecialCards")
    public ResponseEntity<List<SpecialCard>> getMainBoardSpecialCards(@PathVariable("code") String code) {

        Game g = gs.getGameByCode(code);
        if (!gs.checkPlayerInGameAndGameExists(g)) {
            return ResponseEntity.notFound().build();
        }
        List<SpecialCard> cd = g.getMainBoard().getSCards();

        return new ResponseEntity<>(cd, HttpStatus.OK);

    }

    @PostMapping("/join/{code}")
    public ResponseEntity<Game> joinGame(@PathVariable("code") String code) {

        Game g = gs.getGameByCode(code);

        User u = us.findCurrentUser();

        // if a player already exists in a game he can just join the game :)
        if (g == null || u == null) {
            // TODO: Create error
            return ResponseEntity.notFound().build();
        }

        if (g.getStart() != null) {
            // TODO: Create error
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        if (!gs.gameContainsPlayer(g, u)) {
            Player p = ps.initialize(u.getUsername());
            p.setColor(ps.getRandomColor(g.getPlayers()));

            p.setUser(u);
            p.setObjects(new ArrayList<Object>());

            ps.savePlayer(p);
            gs.addPlayer(g, p);
        } else {
            // TODO: Create error
            System.out.println("This player already in game");
        }

        return ResponseEntity.ok(g);
    }

    @PostMapping("/joinPublic/{id}")
    public ResponseEntity<Game> joinPublicGame(@PathVariable("id") Integer id) {

        Optional<Game> g_tmp = gs.getGameById(id);

        if (g_tmp.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Game g = g_tmp.get();

        if (g.getIsPublic() == false || g.getStart() != null) {
            // TODO: Create error
            // El juego ya ha comenzado
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User u = us.findCurrentUser();
        // if a player already exists in a game he cant just join the game :)

        if (!gs.gameContainsPlayer(g, u)) {
            Player p = ps.initialize(u.getUsername());
            p.setColor(ps.getRandomColor(g.getPlayers()));
            p.setUser(u);
            p.setObjects(new ArrayList<Object>());

            ps.savePlayer(p);
            gs.addPlayer(g, p);
        } else {
            // TODO: Create error
            System.out.println("This player already in game");
        }

        return ResponseEntity.ok(g);
    }

    @PostMapping("/spectate/{code}")
    public ResponseEntity<Game> spectateGame(@PathVariable("code") String code) {

        Game g = gs.getGameByCode(code);

        User u = us.findCurrentUser();

        // if a player already exists in a game he can just join the game :)

        if (g == null || u == null) {
            // TODO: Create error
            return ResponseEntity.notFound().build();
        }
        if (g.getStart() != null) {
            // TODO: Create error
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (!gs.gameContainsSpectator(g, u)) {
            Spectator s = specservice.initialize(u.getUsername());

            s.setColor(ps.getRandomColor(g.getPlayers()));

            s.setUser(u);

            specservice.saveSpectator(s);
            gs.addSpectator(g, s);
        } else {
            // TODO: Create error
            System.out.println("This player already in game");
        }

        return ResponseEntity.ok(g);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Game> createGame(@Valid @RequestBody Game g) {

        User u = us.findCurrentUser();

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

        gs.saveGame(g);

        // TODO: revisar
        g.setMainBoard(null);

        return new ResponseEntity<>(g, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateGame(@Valid @RequestBody Game g, @PathVariable("id") Integer id) {
        Game gToUpdate = getGameById(id);
        BeanUtils.copyProperties(g, gToUpdate, "id", "playerCreator", "playerStart");
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

        if (!gs.checkPlayerInGameAndGameExists(g)) {
            return ResponseEntity.notFound().build();
        }

        Integer round = g.getRound();

        List<Dwarf> dwarves = g.getDwarves();
        dwarves = dwarves.stream().filter(d -> d.getRound() == round).toList();

        return new ResponseEntity<>(dwarves, HttpStatus.OK);
    }

    @GetMapping("/play/{code}/players")
    public ResponseEntity<List<Player>> getPlayersFromGame(@PathVariable("code") String code) {

        Game g = gs.getGameByCode(code);
        if (!gs.checkPlayerInGameAndGameExists(g)) {
            return ResponseEntity.notFound().build();
        }

        return new ResponseEntity<>(g.getPlayers(), HttpStatus.OK);
    }

    @GetMapping("/play/{code}/isMyTurn")
    public ResponseEntity<Boolean> getTurn(@PathVariable("code") String code) {
        // Gets the lists of players and dwarfs. Turn is the next player
        Boolean res = false;
        Game g = gs.getGameByCode(code);
        if (!gs.checkPlayerInGameAndGameExists(g)) {
            return ResponseEntity.notFound().build();
        }

        List<Player> plys = g.getPlayers();
        if (plys.size() < 1) {
            System.out.println("No players");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User u = us.findCurrentUser();
        Player p = gs.getPlayerByUserAndGame(u, g);
        if (p == null) {
            System.out.println("no player");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Dwarf> dwarves = g.getDwarves();
        dwarves = dwarves.stream().filter(d -> d.getRound() == g.getRound() && d.getPlayer() != null).toList();
        if (gs.checkRoundNeedsChange(g, dwarves)) {
            gs.handleRoundChange(g);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }

        List<Player> dwarves_players = dwarves.stream().map(d -> d.getPlayer()).toList();
        System.out.println(dwarves_players);

        ArrayList<Player> remaining_turns = new ArrayList<Player>();
        remaining_turns.addAll(gs.getRemainingTurns(plys, dwarves, g.getPlayerStart()));

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
            if (remaining_turns.get(0).equals(p)) {
                res = true;
            }
        }

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/play/{code}/isFinished")
    public ResponseEntity<Boolean> endGame(@PathVariable("code") String code) {

        Game g = gs.getGameByCode(code);
        if (!gs.checkPlayerInGameAndGameExists(g)) {
            return ResponseEntity.notFound().build();
        }

        List<Player> plys = g.getPlayers();

        Boolean finished = false;

        // tiene que terminar si un jugador consigue 4 objetos
        for (Player p : plys) {
            if (p.getObjects() != null && p.getObjects().size() >= 4)
                finished = true;
        }

        // tiene que terminar en 6 rondas
        if (g.getRound() >= 2)
            finished = true;

        if (g.getFinish() != null)
            finished = true;

        return new ResponseEntity<>(finished, HttpStatus.OK);
    }

    @PostMapping("/play/{code}/dwarves")
    public ResponseEntity<Void> addDwarves(@Valid @RequestBody Card card, @PathVariable("code") String code) {

        Game g = gs.getGameByCode(code);
        if (!gs.checkPlayerInGameAndGameExists(g)) {
            return ResponseEntity.notFound().build();
        }

        Dwarf dwarf = new Dwarf();
        Player p = gs.getPlayerByUserAndGame(us.findCurrentUser(), g);

        dwarf.setPlayer(p);
        dwarf.setRound(g.getRound());
        dwarf.setCard(card);

        ds.saveDwarf(dwarf);

        List<Dwarf> dwarves = g.getDwarves();
        dwarves.add(dwarf);
        g.setDwarves(dwarves);

        /*
         * if (gs.checkRoundNeedsChange(g,dwarves)) {
         * try {
         * g = gs.handleRoundChange(g);
         * } catch (Exception e) {
         * System.out.println(e);
         * }
         * }
         */

        gs.saveGame(g);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping("/play/{code}/specialAction")
    public ResponseEntity<Void> handleSpecialAction(@Valid @RequestBody SpecialCardRequestHandler request,
            @PathVariable("code") String code) {

        Game g = gs.getGameByCode(code);
        if (!gs.checkPlayerInGameAndGameExists(g)) {
            return ResponseEntity.notFound().build();
        }

        SpecialCard specialCard = request.getSpecialCard();
        Boolean usesBothDwarves = request.getUsesBothDwarves();

        Player p = gs.getPlayerByUserAndGame(us.findCurrentUser(), g);

        if (usesBothDwarves) {
            Dwarf dwarf1 = new Dwarf();
            Dwarf dwarf2 = new Dwarf();

            dwarf1.setPlayer(p);
            dwarf1.setRound(g.getRound());
            dwarf1.setCard(null);

            dwarf2.setPlayer(p);
            dwarf2.setRound(g.getRound());
            dwarf2.setCard(null);

            ds.saveDwarf(dwarf1);
            ds.saveDwarf(dwarf2);

            List<Dwarf> gameDwarves = g.getDwarves();
            gameDwarves.add(dwarf1);
            gameDwarves.add(dwarf2);
            g.setDwarves(gameDwarves);
        } else {
            if (p.getMedal() > 4) {
                p.setMedal(p.getMedal() - 4);
                ps.savePlayer(p);
            } else {
                // TODO: create error
            }

            Dwarf dwarf1 = new Dwarf();

            dwarf1.setPlayer(p);
            dwarf1.setRound(g.getRound());
            dwarf1.setCard(null);

            ds.saveDwarf(dwarf1);

            List<Dwarf> gameDwarves = g.getDwarves();
            gameDwarves.add(dwarf1);
        }

        // Ahora vamos a darle la vuelta a la carta. Si hay
        // un jugador en esa posicion, es decir, si se hay una entidad
        // dwarf en la base de datos con esa carta, se resuelve automaticamente.
        // Despues, se coloca el dwarf del jugador que ha tirado la carta especial
        // en esa posicion.
        Card reverseCard = specialCard.getTurnedSide();
        List<Dwarf> roundDwarves = g.getDwarves();
        roundDwarves = roundDwarves.stream()
                .filter(d -> d.getRound() == g.getRound() && d.getPlayer() != null && d.getCard() != null).toList();

        for (Dwarf d : roundDwarves) {
            Card dwarfCard = d.getCard();
            if (reverseCard.getPosition().equals(dwarfCard.getPosition())) {
                // Solo tiene sentido resolver esta situacion
                // cuando la carta es de forja o una carta normal
                // El dwarf tiene que permanecer para seguir con el recuento de turnos
                mbs.applySingleCardWhenSpecialCardAction(p, dwarfCard);
            }
        }

        MainBoard mb = g.getMainBoard();
        ArrayList<Location> newLocations = new ArrayList<>();
        newLocations.addAll(mb.getLocations());
        Location locationToUpdate = newLocations.get(reverseCard.getPosition() - 1);
        locationToUpdate = ls.pushCard(locationToUpdate, reverseCard);
        newLocations.set(reverseCard.getPosition() - 1, locationToUpdate);
        mb.setLocations(newLocations);
        mbs.saveMainBoard(mb);

        g.setMainBoard(mb);
        gs.saveGame(g);

        // Ahora aplicamos la carta
        switch (specialCard.getName()) {
            case "Muster an army":
                List<Card> gameCards = g.getMainBoard().getCards();
                ArrayList<Dwarf> gameDwarves = new ArrayList<>(g.getDwarves());
                for (Card c : gameCards) {
                    if (c.getCardType().getName().equals("orcCard")) {
                        Dwarf d = new Dwarf();
                        d.setCard(c);
                        d.setRound(g.getRound());
                        gameDwarves.add(d);
                    }
                }
                g.setDwarves(gameDwarves);
                gs.saveGame(g);
                break;
            case "Special Order":
                Integer selectedGold = request.getSelectedGold();
                Integer selectedIron = request.getSelectedIron();
                Integer selectedSteal = request.getSelectedSteal();
                Object selectedObject = request.getSelectedObject();
                if (selectedGold != null && selectedIron != null
                        && selectedSteal != null && selectedObject != null) {
                    // Check if the sum of gold, iron, and steel is 5
                    if (selectedGold + selectedIron + selectedSteal == 5) {

                        // Check if at least one of each material is selected
                        if (selectedGold > 0 && selectedIron > 0 && selectedSteal > 0) {

                            // Update player's state
                            p.setGold(p.getGold() - selectedGold);
                            p.setIron(p.getIron() - selectedIron);
                            p.setSteal(p.getSteal() - selectedSteal);

                            // Add the selected object
                            p.getObjects().add(selectedObject);

                            // Save the updated player
                            ps.savePlayer(p);

                            return ResponseEntity.ok().build();
                        }
                    }
                }
                break;
            case "Hold a council":
                mb = mbs.holdACouncilAction(mb);
                break;

            case "Collapse the Shafts":
                mbs.collapseTheShaftsAction(mb);
                break;

            case "Run Amok":
                mb = mbs.runAmokAction(mb);
                mbs.saveMainBoard(mb);
                g.setMainBoard(mb);
                gs.saveGame(g);
                break;

            case "Sell an Item":

                Object selectedObject2 = request.getSelectedObject();
                Integer selectedGold2 = request.getSelectedGold();
                Integer selectedIron2 = request.getSelectedIron();
                Integer selectedSteal2 = request.getSelectedSteal();
                if (selectedGold2 != null && selectedIron2 != null
                        && selectedSteal2 != null && selectedObject2 != null) {
                    // Check if the sum of gold, iron, and steel is 5
                    if (selectedGold2 + selectedIron2 + selectedSteal2 == 5) {

                        // Check if at least one of each material is selected
                        if (selectedGold2 > 0 && selectedIron2 > 0 && selectedSteal2 > 0) {

                            // Update player's state
                            p.setGold(p.getGold() + selectedGold2);
                            p.setIron(p.getIron() + selectedIron2);
                            p.setSteal(p.getSteal() + selectedSteal2);

                            // Remove the selected object
                            p.getObjects().remove(selectedObject2);

                            // Save the updated player
                            ps.savePlayer(p);

                            return ResponseEntity.ok().build();
                        }
                    }
                }

                break;

            default:
                break;
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/play/{code}/isStart")
    public ResponseEntity<LocalDateTime> startGame(@PathVariable("code") String code) {
        Game g = gs.getGameByCode(code);
        if (!gs.checkPlayerInGameAndGameExists(g)) {
            return ResponseEntity.notFound().build();
        }

        User u = us.findCurrentUser();
        Player playerStarter = g.getPlayerCreator();
        if (playerStarter.getUser().equals(u) && g.getStart() == null) {

            g.setStart(LocalDateTime.now());
            gs.saveGame(g);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/play/{code}/finish")
    public ResponseEntity<Void> finishGameSetWinner(@PathVariable("code") String code) {

        Game g = gs.getGameByCode(code);
        if (!gs.checkPlayerInGameAndGameExists(g)) {
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

    @PostMapping("/play/{code}/resign")
    public ResponseEntity<Void> resignSetWinner(@PathVariable("code") String code) {

        Game g = gs.getGameByCode(code);
        User u = us.findCurrentUser();
        Player p = gs.getPlayerByUserAndGame(u, g);

        if (!gs.checkPlayerInGameAndGameExists(g)) {
            return ResponseEntity.notFound().build();
        }

        g.setFinish(LocalDateTime.now());
        gs.resign(g, p);

        Player winner = gs.getGameWinner(g);
        System.out.println(winner);

        g.setWinner_id(winner.getId());

        gs.saveGame(g);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/publics")
    public ResponseEntity<List<Game>> publicGames() {
        try {
            List<Game> listGame = gs.getAllPublicGames();
            return new ResponseEntity<>(listGame, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/play/{code}/chat")
    public ResponseEntity<Chat> getChat(@PathVariable("code") String code) {
        Game g = gs.getGameByCode(code);
        if (g == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(g.getChat(), HttpStatus.OK);
    }

}
