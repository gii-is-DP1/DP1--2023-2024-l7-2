package org.springframework.samples.dwarf.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.dwarf.auth.payload.request.SignupRequest;
import org.springframework.samples.dwarf.user.AuthoritiesService;
import org.springframework.samples.dwarf.user.User;
import org.springframework.samples.dwarf.user.UserService;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class AuthServiceTests {

	@Autowired
	protected AuthService authService;
	@Autowired
	protected UserService userService;
	@Autowired
	protected AuthoritiesService authoritiesService;

	@Test
	@Transactional
	public void shouldCreateAdminUser() {
		SignupRequest request = createRequest("ADMIN", "admin2");
		int userFirstCount = ((Collection<User>) this.userService.findAll()).size();
		this.authService.createUser(request);
		int userLastCount = ((Collection<User>) this.userService.findAll()).size();
		assertEquals(userFirstCount + 1, userLastCount);
	}

	@Test
	@Transactional
	public void shouldNotCreateUserWithSameUsername() {
		SignupRequest request = createRequest("USER", "sameUsername");
		this.authService.createUser(request);

		SignupRequest duplicateRequest = createRequest("USER", "sameUsername");
		List<User> userList = new ArrayList<>();
		userService.findAll().forEach(u -> {
			if (u.getUsername().equals("sameUsername")) {
				userList.add(u);
			}
		});
		assertEquals(userList.size(), 1);
	}

	private SignupRequest createRequest(String auth, String username) {
		SignupRequest request = new SignupRequest();
		request.setAuthority(auth);
		request.setPassword("prueba");
		request.setUsername(username);

		return request;
	}

}
