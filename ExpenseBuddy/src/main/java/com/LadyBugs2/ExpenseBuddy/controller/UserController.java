package com.LadyBugs2.ExpenseBuddy.controller;

import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.LadyBugs2.ExpenseBuddy.models.entity.User;
import com.LadyBugs2.ExpenseBuddy.models.dto.UserDTO;
import com.LadyBugs2.ExpenseBuddy.service.UserService;

import lombok.AllArgsConstructor;
import org.springframework.ui.Model;

import java.text.ParseException;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class UserController {

	private final UserService userService;
	
	private final ModelMapper modelMapper;

	/*@GetMapping("/users/{id}")
	public String readUser(@PathVariable Long id, Model model){
		model.addAttribute("user", userService.readUser(id));
		model.addAttribute("monthLimitMsg",userService.checkMonthlyLimit());
		return "users";
	}*/

	@GetMapping({"/users","/users/{id}"})
	public String readUserWithoutId(Model model){
		User user = modelMapper.map(userService.getLoggedInUser(), User.class);
		model.addAttribute("user", user);
		model.addAttribute("monthLimitMsg", userService.checkMonthlyLimit());
		return "users";
	}

	@GetMapping("/user-update")
	public String showUpdatePage(Model model) {
		UserDTO existingUser = userService.getLoggedInUser();
		model.addAttribute("existingUser", existingUser);
		model.addAttribute("user", new UserDTO());
		return "update-user";
	}

	@PostMapping ("/user-update")
	public String updateUser(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult result, Model model){
		/*if(result.hasErrors() || !userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
			UserDTO existingUser = userService.getLoggedInUser();
			model.addAttribute("existingUser", existingUser);
			model.addAttribute("user", new UserDTO());
			return "update-user"; //iz nekog razloga se ne ispisuju greske inputa
		}*/
		User user = userService.updateUser(userDTO);
		return "redirect:/users/"+user.getId();
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<HttpStatus> deleteUser(@PathVariable long id){
		userService.deleteUser(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
