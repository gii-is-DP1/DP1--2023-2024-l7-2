package org.springframework.samples.dwarf.friendRequest;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.dwarf.exceptions.BadRequestException;
import org.springframework.samples.dwarf.exceptions.ResourceNotFoundException;
import org.springframework.samples.dwarf.game.Game;
import org.springframework.samples.dwarf.invitation.Invitation;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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

		if (u.getAuthority().getAuthority().equals("ADMIN")) {
			res = friendRequestService.findAll();
		} else {
			res = friendRequestService.findByUser(u);
		}

		return new ResponseEntity<>(res, HttpStatus.OK);

	}

	@GetMapping("/accepted")
	public ResponseEntity<List<FriendRequest>> findAllAccepted() {
		User u = us.findCurrentUser();

		if (u == null) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

		List<FriendRequest> res = null;

		if (u.getAuthority().getAuthority().equals("ADMIN")) {
			res = friendRequestService.findAll();
		} else {
			res = friendRequestService.findByUser(u).stream().filter(fr -> fr.getStatus().equals(Status.ACCEPTED))
					.toList();
		}

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@GetMapping("/friendGames")
	public ResponseEntity<List<Game>> findAllFriendGames() {
		User u = us.findCurrentUser();

		if (u == null) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

		List<Game> res = friendRequestService.getAllFriendGames(u);

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@GetMapping("/pending")
	public ResponseEntity<List<FriendRequest>> findAllPending() {
		User u = us.findCurrentUser();

		if (u == null) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

		List<FriendRequest> res = null;

		if (u.getAuthority().getAuthority().equals("ADMIN")) {
			res = friendRequestService.findAll();
		} else {
			res = friendRequestService.findByUser(u).stream().filter(fr -> fr.getStatus().equals(Status.SENT)
					&& fr.getReceiver().equals(u))
					.toList();
			;
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
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<FriendRequest> create(@RequestBody @Valid String username) {

		User sender = us.findCurrentUser();
		User receiver = us.findUser(username);
		FriendRequest savedRequest = new FriendRequest();

		savedRequest.setReceiver(receiver);
		savedRequest.setSendTime(LocalDateTime.now());
		savedRequest.setSender(sender);
		savedRequest.setStatus(Status.SENT);
		friendRequestService.saveFriendRequest(savedRequest);

		return new ResponseEntity<>(savedRequest, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteFriendRequest(@PathVariable("id") int id) {
		FriendRequest friendRequestToGet = friendRequestService.findById(id);
		if (friendRequestToGet == null)
			throw new ResourceNotFoundException("FriendRequest with id " + id + " not found!");
		friendRequestService.deleteFriendRequest(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/{username}")
	public ResponseEntity<List<User>> getFriends(@RequestParam @Valid String username) {

		List<User> res = friendRequestService.getFriends(us.findUser(username));

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@PutMapping("/{id}/deny")
	public ResponseEntity<Void> deny(@PathVariable("id") Integer id) {
		FriendRequest friendRequestToUpdate = friendRequestService.findById(id);

		friendRequestToUpdate.setStatus(Status.DENIED);
		friendRequestService.saveFriendRequest(friendRequestToUpdate);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/accept")
	public ResponseEntity<Void> accept(@PathVariable("id") Integer id) {
		FriendRequest friendRequestToUpdate = friendRequestService.findById(id);

		friendRequestToUpdate.setStatus(Status.ACCEPTED);
		friendRequestService.saveFriendRequest(friendRequestToUpdate);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/block")
	public ResponseEntity<Void> block(@PathVariable("id") Integer id) {
		FriendRequest friendRequestToUpdate = friendRequestService.findById(id);

		friendRequestToUpdate.setStatus(Status.BLOCKED);
		friendRequestService.saveFriendRequest(friendRequestToUpdate);

		return ResponseEntity.noContent().build();
	}

}
