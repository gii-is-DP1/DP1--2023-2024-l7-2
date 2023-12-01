package org.springframework.samples.dwarf.friendsRequest;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.dwarf.exceptions.BadRequestException;
import org.springframework.samples.dwarf.exceptions.ResourceNotFoundException;
import org.springframework.samples.dwarf.mainboard.MainBoard;
import org.springframework.samples.dwarf.mainboard.MainBoardService;
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

    private final FriendRequestService friendRequestService;

    @Autowired
	public FriendsController(FriendRequestService friendRequestService) {
		this.friendRequestService = friendRequestService;
	}

    @GetMapping
	public ResponseEntity<List<FriendRequest>> findAll() {
		return new ResponseEntity<>((List<FriendRequest>) friendRequestService.findAll(), HttpStatus.OK);
	}

    @GetMapping("/{id}")
	public ResponseEntity<FriendRequest> findFriendRequest(@PathVariable("id") int id) {
		FriendRequest friendRequestToGet = friendRequestService.findByRequestById(id);
		if (friendRequestToGet == null)
			throw new ResourceNotFoundException("FriendRequest with id " + id + " not found!");
		return new ResponseEntity<FriendRequest>(friendRequestToGet, HttpStatus.OK);
	}

    @DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteFriendRequest(@PathVariable("id") int id) {
		findFriendRequest(id);
		friendRequestService.deleteFriendRequest(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

    @PostMapping
	public ResponseEntity<FriendRequest> createRequest(@RequestBody @Valid FriendRequest sendRequest, BindingResult br) {
		FriendRequest result = null;
		if (!br.hasErrors())
			result = friendRequestService.saveFriendRequest(sendRequest);
		else
			throw new BadRequestException(br.getAllErrors());
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}

    @PutMapping("/{id}")
	public ResponseEntity<Void> modifyStatus(@RequestBody @Valid FriendRequest newStatus, BindingResult br,
			@PathVariable("id") int id) {
		FriendRequest friendRequestToUpdate = this.findFriendRequest(id).getBody();
		if (br.hasErrors())
			throw new BadRequestException(br.getAllErrors());
		else if (newStatus.getId() == null || !newStatus.getId().equals(id))
			throw new BadRequestException("Status id is not consistent with resource URL:" + id);
		else {
			BeanUtils.copyProperties(newStatus, friendRequestToUpdate, "id");
			friendRequestService.saveFriendRequest(friendRequestToUpdate);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
        
}
