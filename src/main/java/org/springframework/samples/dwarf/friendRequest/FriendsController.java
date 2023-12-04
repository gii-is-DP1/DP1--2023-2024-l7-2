package org.springframework.samples.dwarf.friendRequest;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.dwarf.exceptions.BadRequestException;
import org.springframework.samples.dwarf.exceptions.ResourceNotFoundException;
import org.springframework.samples.dwarf.user.User;
import org.springframework.samples.dwarf.user.UserService;
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
@RequestMapping("/api/v1/friends")
@Tag(name = "FriendsRequest", description = "friend request sent by players")
@SecurityRequirement(name = "bearerAuth")
public class FriendsController {

    private FriendRequestService friendRequestService;
	private UserService us;

    @Autowired
	public FriendsController(FriendRequestService friendRequestService, UserService us) {
		this.friendRequestService = friendRequestService;
		this.us = us;
	}

    @GetMapping
	public ResponseEntity<List<FriendRequest>> findAll() {
		User u = us.findCurrentUser();

		if (u == null) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

		List<FriendRequest> res = null;

		if (u.getAuthority().getAuthority().equals("ADMIN"))
		{
			res = friendRequestService.findAll();
		} else {
			res = friendRequestService.findByUser(u);
		}
		
		
		return new ResponseEntity<>(res, HttpStatus.OK);
		
	}

    @GetMapping("/{id}")
	public ResponseEntity<FriendRequest> findFriendRequest(@PathVariable("id") int id) {
		FriendRequest friendRequestToGet = friendRequestService.findById(id);
		if (friendRequestToGet == null)
			throw new ResourceNotFoundException("FriendRequest with id " + id + " not found!");
		return new ResponseEntity<FriendRequest>(friendRequestToGet, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<FriendRequest> createRequest(@Valid @RequestBody FriendRequest sendRequest, BindingResult br) {
		FriendRequest result = null;
		if (!br.hasErrors()){
		result = friendRequestService.saveFriendRequest(sendRequest);
		}else{
			throw new BadRequestException(br.getAllErrors());
		}
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}

    @DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteFriendRequest(@PathVariable("id") int id) {
		FriendRequest friendRequestToGet = friendRequestService.findById(id);
		if (friendRequestToGet == null)
			throw new ResourceNotFoundException("FriendRequest with id " + id + " not found!");
		friendRequestService.deleteFriendRequest(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

    @PutMapping("/{id}")
	public ResponseEntity<Void> modifyFriendRequest(@RequestBody @Valid FriendRequest fr, @PathVariable("id") Integer id) {
		FriendRequest friendRequestToUpdate = friendRequestService.findById(id);
		
			BeanUtils.copyProperties(fr, friendRequestToUpdate, "id");
			friendRequestService.saveFriendRequest(friendRequestToUpdate);
		
		return ResponseEntity.noContent().build();
	}
        
}
