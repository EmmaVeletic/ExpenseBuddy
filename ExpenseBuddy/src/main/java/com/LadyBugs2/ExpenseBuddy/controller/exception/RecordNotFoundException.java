package com.LadyBugs2.ExpenseBuddy.controller.exception;

public class RecordNotFoundException extends RuntimeException {

	
	private static final long serialVersionUID = -7046039672890258427L;

	public RecordNotFoundException(String message) {
		super(message);
	}
	
}
