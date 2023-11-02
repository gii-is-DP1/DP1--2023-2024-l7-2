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
@RequestMapping("/api/v1/card")
@Tag(name = "Cards", description = "The Cards management API")
@SecurityRequirement(name = "bearerAuth")
public class CardRestController {
    
    private final CardService cardService;

    @Autowired
	public CardRestController(CardService cardService) {
		this.cardService = cardService;
	}

    @GetMapping
	public ResponseEntity<List<Card>> findAll() {
		return new ResponseEntity<>((List<Card>) cardService.getCards(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Card> findCard(@PathVariable("id") int id){
		Card cardToGet=cardService.getById(id);
		if(cardToGet==null)
			throw new ResourceNotFoundException("Card with id "+id+" not found!");
		return new ResponseEntity<Card>(cardToGet, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Card> createCard(@RequestBody @Valid Card newCard, BindingResult br){ 
		Card result=null;
		if(!br.hasErrors())
			result=cardService.saveCard(newCard);
		else
			throw new BadRequestException(br.getAllErrors());
		return new ResponseEntity<>(result,HttpStatus.CREATED);	
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> modifyCard(@RequestBody @Valid Card newCard, BindingResult br,@PathVariable("id") int id) {
		Card cardToUpdate=this.findCard(id).getBody();
		if(br.hasErrors())
			throw new BadRequestException(br.getAllErrors());	
		else if(newCard.getId()==null || !newCard.getId().equals(id))
			throw new BadRequestException("Card id is not consistent with resource URL:"+id);
		else{
			BeanUtils.copyProperties(newCard, cardToUpdate, "id");
			cardService.saveCard(cardToUpdate);
		}			
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCard(@PathVariable("id") int id){
		findCard(id);
		cardService.deleteCardById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
