package org.springframework.samples.petclinic.mainboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.exceptions.ResourceNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/{matchId}/MainBoard")
@Tag(name = "MainBoard", description = "Main board on a match")
@SecurityRequirement(name = "bearerAuth")
public class MainBoardRestController {
    private final MainBoardService mainBoardService;

	@Autowired
	public MainBoardRestController(MainBoardService mainBoardService) {
		this.mainBoardService = mainBoardService;
	}

	@GetMapping
	public ResponseEntity<List<MainBoard>> findAll() {
		return new ResponseEntity<>((List<MainBoard>) mainBoardService.getMainBoard(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<MainBoard> getMainBoard(@PathVariable("id") int id) {
		MainBoard achievementToGet = mainBoardService.getById(id);
		if (achievementToGet == null)
			throw new ResourceNotFoundException("Achievement with id " + id + " not found!");
		return new ResponseEntity<MainBoard>(achievementToGet, HttpStatus.OK);
	}
}
