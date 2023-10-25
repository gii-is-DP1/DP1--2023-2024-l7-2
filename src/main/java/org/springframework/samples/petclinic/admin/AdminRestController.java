package org.springframework.samples.petclinic.admin;

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
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin", description = "The Admin management API")
@SecurityRequirement(name = "bearerAuth")
public class AdminRestController {

    private final AdminService adminService;

    @Autowired
	public AdminRestController(AdminService adminService) {
		this.adminService = adminService;
	}

    @GetMapping
	public ResponseEntity<List<Admin>> findAll() {
		return new ResponseEntity<>((List<Admin>) adminService.getAdmin(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Admin> findAdmin(@PathVariable("id") int id){
		Admin adminToGet=adminService.getById(id);
		if(adminToGet==null)
			throw new ResourceNotFoundException("Admin with id "+id+" not found!");
		return new ResponseEntity<Admin>(adminToGet, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Admin> createAdmin(@RequestBody @Valid Admin newAdmin, BindingResult br){
		Admin result=null;
		if(!br.hasErrors())
			result=adminService.saveAdmin(newAdmin);
		else
			throw new BadRequestException(br.getAllErrors());
		return new ResponseEntity<>(result,HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> modifyAdmin(@RequestBody @Valid Admin newAdmin, BindingResult br,@PathVariable("id") int id) {
		Admin adminToUpdate=this.findAdmin(id).getBody();
		if(br.hasErrors())
			throw new BadRequestException(br.getAllErrors());
		else if(newAdmin.getId()==null || !newAdmin.getId().equals(id))
			throw new BadRequestException("Achievement id is not consistent with resource URL:"+id);
		else{
			BeanUtils.copyProperties(newAdmin, adminToUpdate, "id");
			adminService.saveAdmin(adminToUpdate);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteAdmin(@PathVariable("id") int id){
		findAdmin(id);
		adminService.deleteAdminById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    
}
