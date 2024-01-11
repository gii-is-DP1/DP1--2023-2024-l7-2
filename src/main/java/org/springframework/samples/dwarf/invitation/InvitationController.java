package org.springframework.samples.dwarf.invitation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.dwarf.exceptions.BadRequestException;
import org.springframework.samples.dwarf.exceptions.ResourceNotFoundException;
import org.springframework.samples.dwarf.friendRequest.FriendRequest;
import org.springframework.samples.dwarf.game.Game;
import org.springframework.samples.dwarf.game.GameService;
import org.springframework.samples.dwarf.user.User;
import org.springframework.samples.dwarf.user.UserService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/invite")
@Tag(name = "Invitation", description = "Invitation sent by players")
@SecurityRequirement(name = "bearerAuth")
public class InvitationController {
    private final InvitationService is;
    private final GameService gs;
    private final UserService us;

    @Autowired
    public InvitationController(InvitationService is, GameService gs, UserService us) {
        this.is = is;
        this.gs = gs;
        this.us = us;
    }

    @PostMapping("{code}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Invitation> create(@PathVariable("code") String code, @RequestBody @Valid Invitation inv) {

        User u = us.findCurrentUser();

        inv.setGame(gs.getGameByCode(code));
        inv.setSender(u);
        inv.setSendTime(LocalDateTime.now());
        Invitation savedInv = is.saveInvitation(inv);

        return new ResponseEntity<>(savedInv, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Game> getGamesInvitedTo() {

        User u = us.findCurrentUser();
        return is.findGamesInvitedTo(u);
    }

}