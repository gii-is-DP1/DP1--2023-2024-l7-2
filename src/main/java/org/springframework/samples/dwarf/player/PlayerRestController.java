package org.springframework.samples.dwarf.player;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.dwarf.exceptions.BadRequestException;
import org.springframework.samples.dwarf.exceptions.ResourceNotFoundException;
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
@RequestMapping("/api/v1/{matchId}/players")
@Tag(name = "Players", description = "Players on a match")
@SecurityRequirement(name = "bearerAuth")
public class PlayerRestController {

	private final PlayereService playerService;

	@Autowired
	public PlayerRestController(PlayereService playerService) {
		this.playerService = playerService;
	}

	@GetMapping
	public ResponseEntity<List<Player>> findAll() {
		return new ResponseEntity<>((List<Player>) playerService.getPlayers(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Player> getPlayers(@PathVariable("id") int id) {
		Player achievementToGet = playerService.getById(id);
		if (achievementToGet == null)
			throw new ResourceNotFoundException("Achievement with id " + id + " not found!");
		return new ResponseEntity<Player>(achievementToGet, HttpStatus.OK);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Player> createPlayer(@RequestBody @Valid Player newPlayer,
			BindingResult br) {
		Player result = null;
		if (!br.hasErrors())
			result = playerService.savePlayer(newPlayer);
		else
			throw new BadRequestException(br.getAllErrors());
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}

}
