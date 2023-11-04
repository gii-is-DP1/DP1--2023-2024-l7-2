package org.springframework.samples.petclinic.dwarf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.exceptions.BadRequestException;
import org.springframework.samples.petclinic.exceptions.ResourceNotFoundException;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayereService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/{matchId}/Dwarf")
@Tag(name = "Dwarf", description = "Dwarfs on a match")
@SecurityRequirement(name = "bearerAuth")
public class DwarfRestController {

    private final DwarfService dwarfService;

	@Autowired
	public DwarfRestController(DwarfService dwarfService) {
		this.dwarfService = dwarfService;
	}

	@GetMapping
	public ResponseEntity<List<Dwarf>> findAll() {
		return new ResponseEntity<>((List<Dwarf>) dwarfService.getDwarfs(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Dwarf> getDwarfs(@PathVariable("id") int id) {
		Dwarf achievementToGet = dwarfService.getById(id);
		if (achievementToGet == null)
			throw new ResourceNotFoundException("Achievement with id " + id + " not found!");
		return new ResponseEntity<Dwarf>(achievementToGet, HttpStatus.OK);
	}
    
}
