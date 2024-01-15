package org.springframework.samples.dwarf.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
@Getter
public class TooManyPlayersInGameException extends RuntimeException {

	private static final long serialVersionUID = -3906338266891937036L;

	public TooManyPlayersInGameException(final String message) {
		super(message);
	}
}