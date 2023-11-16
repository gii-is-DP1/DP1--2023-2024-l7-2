package org.springframework.samples.petclinic.auth.payload.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
	
	@NotBlank
	private String username;

	@NotBlank
	private String name;
	
	@NotBlank
	private String authority;

	@NotBlank
	private String password;
}
