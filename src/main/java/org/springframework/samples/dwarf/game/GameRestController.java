package org.springframework.samples.dwarf.game;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.dwarf.card.Card;
import org.springframework.samples.dwarf.card.SpecialCard;
import org.springframework.samples.dwarf.chat.Chat;
import org.springframework.samples.dwarf.chat.ChatService;
import org.springframework.samples.dwarf.chat.Message;
import org.springframework.samples.dwarf.dwarf.Dwarf;
import org.springframework.samples.dwarf.dwarf.DwarfService;
import org.springframework.samples.dwarf.exceptions.ResourceNotFoundException;
import org.springframework.samples.dwarf.invitation.Invitation;
import org.springframework.samples.dwarf.invitation.InvitationService;
import org.springframework.samples.dwarf.location.Location;
import org.springframework.samples.dwarf.location.LocationService;
import org.springframework.samples.dwarf.mainboard.MainBoard;
import org.springframework.samples.dwarf.mainboard.MainBoardService;
import org.springframework.samples.dwarf.object.Object;
import org.springframework.samples.dwarf.player.Player;
import org.springframework.samples.dwarf.player.PlayerService;
import org.springframework.samples.dwarf.spectator.Spectator;
import org.springframework.samples.dwarf.spectator.SpectatorService;
import org.springframework.samples.dwarf.user.User;
import org.springframework.samples.dwarf.user.UserService;
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
    private final LocationService ls;
    private final DwarfService ds;
    private final SpectatorService specservice;
    private final ChatService chatservice;
    private final InvitationService is;

    @Autowired
    public GameRestController(GameService gs, UserService us, PlayerService ps,
            MainBoardService mbs, DwarfService ds,
            LocationService ls, SpectatorService specservice, ChatService chatservice, InvitationService is) {
        this.gs = gs;
        this.us = us;
        this.ps = ps;
        this.mbs = mbs;
        this.ds = ds;
        this.ls = ls;
        this.specservice = specservice;
        this.chatservice = chatservice;
        this.is = is;
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

    @GetMapping("/play/{code}/getAllCards")
    public ResponseEntity<ArrayList<List<Card>>> getAllCardsFromAllPositions(@PathVariable("code") String code) {
        Game g = gs.getGameByCode(code);

        if (!gs.checkPlayerInGameAndGameExists(g)) {
            return ResponseEntity.notFound().build();
        }

        ArrayList<List<Card>> res = new ArrayList<>();
        List<Location> locations = g.getMainBoard().getLocations();

        for (Location l : locations) {
            res.add(l.getCards());
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
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
            
            return ResponseEntity.notFound().build();
        }

        if (g.getStart() != null) {
            
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
            
           return ResponseEntity.status(HttpStatus.CONFLICT).body(g);
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
            return ResponseEntity.status(HttpStatus.CONFLICT).body(g);
            
        }

        return ResponseEntity.ok(g);
    }

    @PostMapping("/spectate/{code}")
    public ResponseEntity<Game> spectateGame(@PathVariable("code") String code) {

        Game g = gs.getGameByCode(code);

        User u = us.findCurrentUser();

        // if a player already exists in a game he can just join the game :)

        if (g == null || u == null) {
            
            return ResponseEntity.notFound().build();
        }
        if (g.getStart() != null) {
            
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (!gs.gameContainsSpectator(g, u)) {
            Spectator s = specservice.initialize(u.getUsername());

            s.setColor(ps.getRandomColor(g.getPlayers()));

            s.setUser(u);

            specservice.saveSpectator(s);
            gs.addSpectator(g, s);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(g);
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
            if (p.getObjects() != null && p.getObjects().size() >= 4) {
                finished = true;
                break;
            }
        }

        // tiene que terminar en 6 rondas
        // if (finished || g.getRound() >= 6)
        // finished = true;

        if (finished || g.getFinish() != null)
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
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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
        Integer round = g.getRound();
        roundDwarves = roundDwarves.stream()
                .filter(d -> d.getRound() == round && d.getPlayer() != null && d.getCard() != null).toList();

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

        mb = mbs.removeUsedSpecialCard(mb, specialCard);
        mb = mbs.applyReverseSpecialCard(mb, reverseCard);
        mb = mbs.saveMainBoard(mb);
        g.setMainBoard(mb);
        gs.saveGame(g);

        
        Integer selectedPosition, selectedGold, selectedIron, 
            selectedSteal;
        Object selectedObject;
        // Ahora aplicamos la carta
        switch (specialCard.getName()) {
            case "Muster an army":
                List<Card> gameCards = g.getMainBoard().getCards();
                ArrayList<Dwarf> gameDwarves = new ArrayList<>(g.getDwarves());
                for (Card c : gameCards) {
                    if (c.getCardType().getName().equals("OrcCard")) {
                        Dwarf d = new Dwarf();
                        d.setCard(c);
                        d.setRound(round);
                        d = ds.saveDwarf(d);
                        gameDwarves.add(d);
                    }
                }
                g.setDwarves(gameDwarves);
                gs.saveGame(g);
                break;
            case "Special order":
                selectedGold = request.getSelectedGold();
                selectedIron = request.getSelectedIron();
                selectedSteal = request.getSelectedSteal();
                selectedObject = request.getSelectedObject();
                if (selectedGold != null && selectedIron != null
                        && selectedSteal != null && selectedObject != null
                        && selectedGold + selectedIron + selectedSteal == 5
                        && selectedGold > 0 && selectedIron > 0 && selectedSteal > 0) {

                    List<Object> playerObjects = p.getObjects();
                    // Update player's state
                    if (selectedGold > p.getGold() || selectedIron > p.getIron()
                            || selectedSteal > p.getSteal() || playerObjects.contains(selectedObject)) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                    }
                    p.setGold(p.getGold() - selectedGold);
                    p.setIron(p.getIron() - selectedIron);
                    p.setSteal(p.getSteal() - selectedSteal);

                    // Add the selected object
                    playerObjects.add(selectedObject);
                    p.setObjects(playerObjects);

                    // Save the updated player
                    ps.savePlayer(p);

                    return ResponseEntity.ok().build();
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

                selectedObject = request.getSelectedObject();
                selectedGold = request.getSelectedGold();
                selectedIron = request.getSelectedIron();
                selectedSteal = request.getSelectedSteal();
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
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                    }
                    playerObjects.remove(selectedObject);
                    p.setObjects(playerObjects);
                    // Save the updated player
                    ps.savePlayer(p);
                    return ResponseEntity.ok().build();
                        
                    
                }

                break;

            case "Apprentice":

                selectedPosition = request.getPosition();
                List<Dwarf> roundDwarvesApprentice = g.getDwarves();
                roundDwarvesApprentice = roundDwarvesApprentice.stream()
                        .filter(d -> d.getRound() == round && d.getPlayer() != null && d.getCard() != null).toList();

                // Lógica para manejar la acción de la carta "Apprentice"
                for (Dwarf d : roundDwarvesApprentice) {

                    if (!d.getPlayer().equals(p) && d.getCard().getPosition().equals(selectedPosition)) {
                        // Coloca un nuevo enano en la misma posición
                        Dwarf newDwarf = new Dwarf();
                        newDwarf.setPlayer(p);
                        newDwarf.setRound(g.getRound());
                        newDwarf.setCard(d.getCard());

                        ds.saveDwarf(newDwarf);

                        List<Dwarf> gameDwarvesApprentice = g.getDwarves();
                        gameDwarvesApprentice.add(newDwarf);
                        g.setDwarves(gameDwarvesApprentice);
                        gs.saveGame(g);
                    }
                }
                break;

            case "Turn back":
                // Lógica para manejar la acción de la carta "Turn back"
                List<Location> newLocationsTurnBack = new ArrayList<>(mb.getLocations());
                selectedPosition = request.getPosition();

                if (selectedPosition >= 1 && selectedPosition <= newLocationsTurnBack.size()) {
                    Location selectedLocation = newLocationsTurnBack.get(selectedPosition - 1);

                    Card removedCard = ls.removeLastCard(selectedLocation);
                    if (removedCard != null) {
                        List<Card> newCards = selectedLocation.getCards();
                        Card newTopCard = newCards.get(newCards.size() - 1);

                        Dwarf newDwarfTurnBack = new Dwarf();
                        newDwarfTurnBack.setPlayer(p);
                        newDwarfTurnBack.setRound(g.getRound());
                        newDwarfTurnBack.setCard(newTopCard);

                        ds.saveDwarf(newDwarfTurnBack);

                        List<Dwarf> gameDwarvesTurnBack = new ArrayList<>(g.getDwarves());
                        gameDwarvesTurnBack.add(newDwarfTurnBack);
                        g.setDwarves(gameDwarvesTurnBack);

                        gs.saveGame(g);

                    }
                } else {
                     return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }

                break;
            case "Past Glories":
                selectedPosition = request.getPosition();
                Card cardToBeOnTop = request.getPastCard();

                if (selectedPosition != null && cardToBeOnTop != null && selectedPosition >= 1
                        && selectedPosition <= 9) {

                    Location selectedLocation = mb.getLocations().get(selectedPosition - 1);

                    ls.pastGloriesAction(selectedLocation, cardToBeOnTop);

                } else {
                    // TODO: create error
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
    public Page<Game> publicGamesPagination(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "3") int pageSize) {

        User u = us.findCurrentUser();
        if (u == null) {
            return null;
        }
        Pageable pageable = PageRequest.of(page, pageSize);
        return gs.getAllPublicGames(pageable);
    }

    @GetMapping("/play/{code}/chat")
    public ResponseEntity<List<Message>> getChat(@PathVariable("code") String code) {
        Game g = gs.getGameByCode(code);
        if (g == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(g.getChat().getMessages(), HttpStatus.OK);
    }

    @PostMapping("/play/{code}/chat")
    public ResponseEntity<Chat> receiveMessage(@PathVariable("code") String code, @Valid @RequestBody Message msg) {
        Game g = gs.getGameByCode(code);
        if (g == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User u = us.findCurrentUser();

        msg.setSentTime(LocalDateTime.now());
        msg.setSender(u);

        chatservice.saveMessage(g.getChat(), msg);

        return new ResponseEntity<>(g.getChat(), HttpStatus.OK);
    }

    @PostMapping("/play/{code}/invite")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Invitation> create(@PathVariable("code") String code, @RequestBody @Valid String username) {

        Invitation inv = new Invitation();
        User u1 = us.findCurrentUser();
        User u2 = us.findUser(username);

        inv.setGame(gs.getGameByCode(code));
        inv.setSender(u1);
        inv.setReceiver(u2);
        inv.setSendTime(LocalDateTime.now());
        Invitation savedInv = is.saveInvitation(inv);

        return new ResponseEntity<>(savedInv, HttpStatus.CREATED);
    }

}
