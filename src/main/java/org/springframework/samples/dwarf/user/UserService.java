/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.dwarf.user;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.dwarf.exceptions.ResourceNotFoundException;
import org.springframework.samples.dwarf.friendRequest.FriendRequestService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

	private final PasswordEncoder encoder;
	private UserRepository userRepository;
	private FriendRequestService friendRequestService;

	@Autowired
	public UserService(UserRepository userRepository, FriendRequestService friendRequestService, PasswordEncoder encoder) {
		this.userRepository = userRepository;
		this.friendRequestService = friendRequestService;
		this.encoder = encoder;
	}

	@Transactional
	public User saveUser(User user) throws DataAccessException {
		userRepository.save(user);
		return user;
	}

	@Transactional(readOnly = true)
	public User findUser(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
	}

	@Transactional(readOnly = true)
	public User findUserById(Integer id) {
		return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
	}

	@Transactional(readOnly = true)
	public User findCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null)
			throw new ResourceNotFoundException("Nobody authenticated!");
		else
			return userRepository.findByUsername(auth.getName())
					.orElseThrow(() -> new ResourceNotFoundException("User", "Username", auth.getName()));
	}

	public Boolean existsUser(String username) {
		return userRepository.existsByUsername(username);
	}

	@Transactional(readOnly = true)
	public Iterable<User> findAll() {
		return userRepository.findAll();
	}

	public Iterable<User> findAllByAuthority(String auth) {
		return userRepository.findAllByAuthority(auth);
	}

	@Transactional
	public User updateUser(@Valid User user, Integer idToUpdate) {
		User toUpdate = findUserById(idToUpdate);
		toUpdate.setUsername(user.getUsername());
		toUpdate.setPassword(encoder.encode(user.getPassword()));
		userRepository.save(toUpdate);

		return toUpdate;
	}

	@Transactional
	public void deleteUser(Integer id) {
		User toDelete = findUserById(id);
		friendRequestService.deleteAllFriendRequests(toDelete);
		this.userRepository.delete(toDelete);
	}

	public User userIsLoogedIn(String username) {
		User toUpdate = findUser(username);
		toUpdate.setIsLoggedIn(true);
		userRepository.save(toUpdate);

		return toUpdate;
	}

	public User userIsNotLoogedIn(String username) {
		User toUpdate = findUser(username);
		toUpdate.setIsLoggedIn(false);
		userRepository.save(toUpdate);

		return toUpdate;
	}

	@Transactional(readOnly = true)
	public List<User> findIsLogged(String username) {
		User currentUser = findUser(username);

		List<User> res = friendRequestService.getFriends(currentUser);

		return res.stream().filter(u -> u.getIsLoggedIn()).toList();
	}

}
