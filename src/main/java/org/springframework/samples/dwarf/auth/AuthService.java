package org.springframework.samples.dwarf.auth;

import java.util.ArrayList;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.dwarf.auth.payload.request.SignupRequest;
import org.springframework.samples.dwarf.user.Authorities;
import org.springframework.samples.dwarf.user.AuthoritiesService;
import org.springframework.samples.dwarf.user.User;
import org.springframework.samples.dwarf.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private final PasswordEncoder encoder;
	private final AuthoritiesService authoritiesService;
	private final UserService userService;

	@Autowired
	public AuthService(PasswordEncoder encoder, AuthoritiesService authoritiesService, UserService userService) {
		this.encoder = encoder;
		this.authoritiesService = authoritiesService;
		this.userService = userService;
	}

	@Transactional
	public void createUser(@Valid SignupRequest request) {
		User user = new User();
		user.setName(request.getName());
		user.setUsername(request.getUsername());
		user.setPassword(encoder.encode(request.getPassword()));
		String strRoles = request.getAuthority();
		user.setAuthority(authoritiesService.findByAuthority(strRoles));
		userService.saveUser(user);

	}

}
