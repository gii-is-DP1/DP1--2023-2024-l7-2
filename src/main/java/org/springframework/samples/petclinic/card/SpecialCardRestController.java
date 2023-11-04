package org.springframework.samples.petclinic.card;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.exceptions.BadRequestException;
import org.springframework.samples.petclinic.exceptions.ResourceNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/specialcard")
@Tag(name = "Special Cards", description = "The Special Cards management API")
@SecurityRequirement(name = "bearerAuth")
public class SpecialCardRestController {
    
    private final SpecialCardService specialCardService;

    @Autowired
	public SpecialCardRestController(SpecialCardService specialCardService) {
		this.specialCardService = specialCardService;
	}

    @GetMapping
	public ResponseEntity<List<SpecialCard>> findAll() {
		return new ResponseEntity<>((List<SpecialCard>) specialCardService.getSpecialCards(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<SpecialCard> findSpecialCard(@PathVariable("id") int id){
		SpecialCard specialCardToGet=specialCardService.getById(id);
		if(specialCardToGet==null)
			throw new ResourceNotFoundException("Card with id "+id+" not found!");
		return new ResponseEntity<SpecialCard>(specialCardToGet, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<SpecialCard> createSpecialCard(@RequestBody @Valid SpecialCard newSpecialCard, BindingResult br){ 
		SpecialCard result=null;
		if(!br.hasErrors())
			result=specialCardService.saveSpecialCard(newSpecialCard);
		else
			throw new BadRequestException(br.getAllErrors());
		return new ResponseEntity<>(result,HttpStatus.CREATED);	
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> modifySpecialCard(@RequestBody @Valid SpecialCard newSpecialCard, BindingResult br,@PathVariable("id") int id) {
		SpecialCard specialCardToUpdate=this.findSpecialCard(id).getBody();
		if(br.hasErrors())
			throw new BadRequestException(br.getAllErrors());	
		else if(newSpecialCard.getId()==null || !newSpecialCard.getId().equals(id))
			throw new BadRequestException("Special Card id is not consistent with resource URL:"+id);
		else{
			BeanUtils.copyProperties(newSpecialCard, specialCardToUpdate, "id");
			specialCardService.saveSpecialCard(specialCardToUpdate);
		}			
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSpecialCard(@PathVariable("id") int id){
		findSpecialCard(id);
		specialCardService.deleteSpecialCardById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
