package com.LadyBugs2.ExpenseBuddy.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)

public class ItemAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ItemAlreadyExistsException(String message) {
		super(message);
	}
}
