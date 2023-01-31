package com.LadyBugs2.ExpenseBuddy.service;

import com.LadyBugs2.ExpenseBuddy.models.entity.User;
import com.LadyBugs2.ExpenseBuddy.models.dto.UserDTO;

import java.text.ParseException;
import java.util.Optional;

public interface UserService {

	UserDTO createUser(UserDTO user) throws ParseException;

	UserDTO getDTOById(long id);

	User readUser(Long id);

	User updateUser(UserDTO user);

	void deleteUser(long id);

	UserDTO getDTOByEmail(String email);

	boolean existsByEmail(String email);

	UserDTO getLoggedInUser();

	boolean checkMonthlyLimit();
}
