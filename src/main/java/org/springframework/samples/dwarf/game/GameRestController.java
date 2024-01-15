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
import org.springframework.samples.dwarf.exceptions.ExistingUserException;
import org.springframework.samples.dwarf.exceptions.GameAlreadyStartedException;
import org.springframework.samples.dwarf.exceptions.ResourceNotFoundException;
import org.springframework.samples.dwarf.exceptions.TooManyPlayersInGameException;
import org.springframework.samples.dwarf.exceptions.WrongTurnException;
import org.springframework.samples.dwarf.exceptions.AccessDeniedException;
import org.springframework.samples.dwarf.exceptions.CannotUseCardException;
import org.springframework.samples.dwarf.invitation.Invitation;
import org.springframework.samples.dwarf.invitation.InvitationService;
import org.springframework.samples.dwarf.location.Location;
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

    private final Integer MAX_PLAYERS_IN_GAME = 3;

    private final GameService gs;
    private final UserService us;
    private final PlayerService ps;
    private final SpectatorService specservice;
    private final ChatService chatservice;
    private final InvitationService is;

    @Autowired
    public GameRestController(GameService gs, UserService us, PlayerService ps,
            SpectatorService specservice, ChatService chatservice, InvitationService is) {
        this.gs = gs;
        this.us = us;
        this.ps = ps;
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
            throw new ResourceNotFoundException("Game not found");
        return g.get();
    }

    @GetMapping("/play/{code}")
    public ResponseEntity<Game> playGame(@PathVariable("code") String code) {

        Game g = gs.getGameByCode(code);

        if (g == null) {
            throw new ResourceNotFoundException("Game not found");
        }

        g.setDwarves(null);

        return new ResponseEntity<>(g, HttpStatus.OK);
    }

    @GetMapping("/play/{code}/cards")
    public ResponseEntity<List<Card>> getCardsFromMainBoardFromGame(@PathVariable("code") String code) {
        Game g = gs.getGameByCode(code);

        if (!gs.checkPlayerInGameAndGameExists(g)) {
            throw new ExistingUserException("Player not in the game");
        }

        List<Card> cards = g.getMainBoard().getCardDeck().getCards();

        cards = cards.stream().sorted((c1, c2) -> c1.getPosition().compareTo(c2.getPosition())).toList();

        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    @GetMapping("/play/{code}/getAllCards")
    public ResponseEntity<ArrayList<List<Card>>> getAllCardsFromAllPositions(@PathVariable("code") String code) {
        Game g = gs.getGameByCode(code);

        if (!gs.checkPlayerInGameAndGameExists(g)) {
            throw new ExistingUserException("Player not in the game");
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
            throw new ExistingUserException("Player not in the game");
        }

        List<Card> cd = g.getMainBoard().getCards();

        return new ResponseEntity<>(cd, HttpStatus.OK);
    }

    @GetMapping("/play/{code}/getSpecialCards")
    public ResponseEntity<List<SpecialCard>> getMainBoardSpecialCards(@PathVariable("code") String code) {

        Game g = gs.getGameByCode(code);
        if (!gs.checkPlayerInGameAndGameExists(g)) {
            throw new ExistingUserException("Player not in the game");

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

            throw new ResourceNotFoundException("Player not in the game");
        }

        if (!gs.gameContainsPlayer(g, u)) {
            if (g.getStart() != null) {
                throw new GameAlreadyStartedException("Game has already started");
            } else if (g.getPlayers().size() >= MAX_PLAYERS_IN_GAME) {
                throw new TooManyPlayersInGameException("Too many players in game");
            }
            gs.joinPlayer(g, u);
        }

        return ResponseEntity.ok(g);
    }

    @PostMapping("/joinPublic/{id}")
    public ResponseEntity<Game> joinPublicGame(@PathVariable("id") Integer id) {

        Optional<Game> g_tmp = gs.getGameById(id);

        if (g_tmp.isEmpty()) {
            throw new AccessDeniedException("User unauthorized");
        }
        Game g = g_tmp.get();

        User u = us.findCurrentUser();
        // if a player already exists in a game he cant just join the game :)

        if (!gs.gameContainsPlayer(g, u)) {
            if (g.getIsPublic() == false) {
                throw new AccessDeniedException("User unauthorized");
            } else if (g.getStart() != null) {

                // El juego ya ha comenzado
                throw new GameAlreadyStartedException("Game has already started");
            } else if (g.getPlayers().size() >= MAX_PLAYERS_IN_GAME) {
                throw new TooManyPlayersInGameException("Too many players in game");
            }
            gs.joinPlayer(g, u);

        }

        return ResponseEntity.ok(g);
    }

    @PostMapping("/spectate/{code}")
    public ResponseEntity<Game> spectateGame(@PathVariable("code") String code) {

        Game g = gs.getGameByCode(code);

        User u = us.findCurrentUser();

        // if a player already exists in a game he can just join the game :)

        if (g == null || u == null) {
            throw new ExistingUserException("Player not in the game");

        }
        if (g.getStart() != null) {

            throw new GameAlreadyStartedException("Game has already started");
        }

        if (!gs.gameContainsSpectator(g, u)) {
            Spectator s = specservice.initialize(u.getUsername());

            s.setColor(ps.getRandomColor(g.getPlayers()));

            s.setUser(u);

            specservice.saveSpectator(s);
            gs.addSpectator(g, s);
        }

        return ResponseEntity.ok(g);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Game> createGame(@Valid @RequestBody Game g) {

        User u = us.findCurrentUser();

        g = gs.initalize(g, u);

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
            throw new ExistingUserException("Player not in the game");
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
            throw new ExistingUserException("Player not in the game");

        }

        return new ResponseEntity<>(g.getPlayers(), HttpStatus.OK);
    }

    @GetMapping("/play/{code}/isMyTurn")
    public ResponseEntity<Player> getTurn(@PathVariable("code") String code) {
        // Gets the lists of players and dwarfs. Turn is the next player
        Game g = gs.getGameByCode(code);
        if (!gs.checkPlayerInGameAndGameExists(g)) {
            throw new ExistingUserException("Player not in the game");
        }

        List<Player> plys = g.getPlayers();

        User u = us.findCurrentUser();
        Player p = gs.getPlayerByUserAndGame(u, g);

        List<Dwarf> dwarves = g.getDwarves();
        dwarves = dwarves.stream().filter(d -> d.getRound() == g.getRound() && d.getPlayer() != null).toList();
        if (gs.checkRoundNeedsChange(g, dwarves)) {
            gs.handleRoundChange(g);
            return new ResponseEntity<>(p, HttpStatus.OK);
        }

        p = gs.getCurrentTurnPlayer(plys, dwarves, g.getPlayerStart());

        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @GetMapping("/play/{code}/isFinished")
    public ResponseEntity<Boolean> endGame(@PathVariable("code") String code) {

        Game g = gs.getGameByCode(code);
        if (!gs.checkPlayerInGameAndGameExists(g)) {
            return ResponseEntity.notFound().build();
        }
        Boolean finished = gs.needsToEnd(g);

        return new ResponseEntity<>(finished, HttpStatus.OK);
    }

    @PostMapping("/play/{code}/dwarves")
    public ResponseEntity<Void> addDwarves(@Valid @RequestBody Card card, @PathVariable("code") String code) {

        Game g = gs.getGameByCode(code);
        if (!gs.checkPlayerInGameAndGameExists(g)) {
            throw new ExistingUserException("Player not in the game");
        }

        Player p = gs.getPlayerByUserAndGame(us.findCurrentUser(), g);
        if (!gs.checkIfIsPlayerTurn(g, p)) {
            throw new WrongTurnException("Not player's turn");
        }
        List<Dwarf> roundDwarves = gs.getRoundDwarfs(g, g.getRound());
        if (!gs.canAddDwarf(p, card, roundDwarves)) {
            throw new CannotUseCardException("Cannot use that card");
        }
        g = gs.addDwarf(g, p, card);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping("/play/{code}/specialAction")
    public ResponseEntity<Void> handleSpecialAction(@Valid @RequestBody SpecialCardRequestHandler request,
            @PathVariable("code") String code) {

        Game g = gs.getGameByCode(code);
        if (!gs.checkPlayerInGameAndGameExists(g)) {
            throw new ExistingUserException("Player not in the game");
        }

        SpecialCard specialCard = request.getSpecialCard();
        Boolean usesBothDwarves = request.getUsesBothDwarves();

        Player p = gs.getPlayerByUserAndGame(us.findCurrentUser(), g);

        gs.handleSpecialCard(g, specialCard, usesBothDwarves, p, request);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/play/{code}/isStart")
    public ResponseEntity<LocalDateTime> startGame(@PathVariable("code") String code) {
        Game g = gs.getGameByCode(code);
        if (!gs.checkPlayerInGameAndGameExists(g)) {
            throw new ExistingUserException("Player not in the game");
        }

        User u = us.findCurrentUser();
        Player playerStarter = g.getPlayerCreator();
        if (playerStarter.getUser().equals(u) && g.getStart() == null) {

            g.setStart(LocalDateTime.now());
            gs.saveGame(g);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new AccessDeniedException("User unauthorized");
        }

    }

    @PostMapping("/play/{code}/finish")
    public ResponseEntity<Void> finishGameSetWinner(@PathVariable("code") String code) {

        Game g = gs.getGameByCode(code);
        if (!gs.checkPlayerInGameAndGameExists(g)) {
            throw new ExistingUserException("Player not in the game");
        }

        g.setFinish(LocalDateTime.now());

        Player p = gs.getGameWinner(g);

        g.setUserWinner(p.getUser());

        gs.saveGame(g);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/play/{code}/winner")
    public ResponseEntity<User> getWinner(@PathVariable("code") String code) {
        Game g = gs.getGameByCode(code);
        if (g == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(g.getUserWinner(), HttpStatus.OK);
    }

    @PostMapping("/play/{code}/resign")
    public ResponseEntity<Void> resignSetWinner(@PathVariable("code") String code) {

        Game g = gs.getGameByCode(code);
        User u = us.findCurrentUser();
        Player p = gs.getPlayerByUserAndGame(u, g);

        if (!gs.checkPlayerInGameAndGameExists(g)) {
            throw new ExistingUserException("Player not in the game");
        }

        g.setFinish(LocalDateTime.now());
        gs.resign(g, p);

        Player winner = gs.getGameWinner(g);

        g.setUserWinner(winner.getUser());

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
            throw new ResourceNotFoundException("Game not found");
        }
        return new ResponseEntity<>(g.getChat().getMessages(), HttpStatus.OK);
    }

    @PostMapping("/play/{code}/chat")
    public ResponseEntity<Chat> receiveMessage(@PathVariable("code") String code, @Valid @RequestBody Message msg) {
        Game g = gs.getGameByCode(code);
        if (g == null) {
            throw new ResourceNotFoundException("Game not found");
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
