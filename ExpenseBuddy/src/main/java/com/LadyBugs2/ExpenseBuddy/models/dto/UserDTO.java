package com.LadyBugs2.ExpenseBuddy.models.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class UserDTO {

	Long id;

	@NotBlank(message = "username field should not be blank")
	@Size(min = 5, message = "username needs to be at least 5 characters long")
	String username;

	//@NotBlank(message = "first name field should not be blank")
	@Size(min = 2, message = "first name needs to be at least 2 characters long")
	String firstName;

	//@NotBlank(message = "last name field should not be blank")
	@Size(min = 2, message = "last name needs to be at least 2 characters long")
	String lastName;

	@NotBlank(message = "email field should not be blank")
	@Email(message =  "email not valid")
	String email;

	//@NotBlank(message = "phone field should not be blank")
	String phone;

	@NotBlank(message = "password field should not be blank")
	@Size(min = 5, message = "password needs to be at least 5 characters long")
	String password;

	@NotBlank(message = "confirmation password field should not be blank")
	String confirmPassword;

	Double dailyLimit;

	Double weeklyLimit;

	Double monthlyLimit;

	Double yearlyLimit;
	

}
