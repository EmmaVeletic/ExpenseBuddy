package com.LadyBugs2.ExpenseBuddy.models.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Error {
	
	private Integer statusCode;
	
	private String message;
	
	private Date timestamp;
	

}
