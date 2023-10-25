package org.springframework.samples.petclinic.object;
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
@RequestMapping("/api/v1/objects")
@Tag(name = "Objects", description = "The Objects of the match")
@SecurityRequirement(name = "bearerAuth")
public class ObjectRestController {
    private final ObjectService objectService;

    @Autowired
	public ObjectRestController(ObjectService objectService) {
		this.objectService = objectService;
	}

     @GetMapping
	public ResponseEntity<List<Object>> findAll() {
		return new ResponseEntity<>((List<Object>) objectService.getObjects(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> findObject(@PathVariable("id") int id){
		Object objectToGet = objectService.getById(id);
		if(objectToGet == null)
			throw new ResourceNotFoundException("Object with id "+id+" not found!");
		return new ResponseEntity<Object>(objectToGet, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Object> createObject(@RequestBody @Valid Object newObject, BindingResult br){ 
		Object result=null;
		if(!br.hasErrors())
			result = objectService.saveObject(newObject);
		else
			throw new BadRequestException(br.getAllErrors());
		return new ResponseEntity<>(result,HttpStatus.CREATED);	
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> modifyObject(@RequestBody @Valid Object newObject, BindingResult br,@PathVariable("id") int id) {
		Object objectToUpdate=this.findObject(id).getBody();
		if(br.hasErrors())
			throw new BadRequestException(br.getAllErrors());	
		else if(newObject.getId()==null || !newObject.getId().equals(id))
			throw new BadRequestException("Object id is not consistent with resource URL:"+id);
		else{
			BeanUtils.copyProperties(newObject, objectToUpdate, "id");
			objectService.saveObject(objectToUpdate);
		}			
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteObject(@PathVariable("id") int id){
		findObject(id);
		objectService.deleteObjectById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
