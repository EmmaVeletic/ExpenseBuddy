package com.LadyBugs2.ExpenseBuddy.controller;

import com.LadyBugs2.ExpenseBuddy.models.dto.UserDTO;
import com.LadyBugs2.ExpenseBuddy.models.entity.User;
import com.LadyBugs2.ExpenseBuddy.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.ParseException;

@Controller
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping({"/login","/"})
    public String showLoginPage(Model model) {
        model.addAttribute("user", new UserDTO());
        return "login";
    }

    @PostMapping({"/login"})
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
    }

    @PostMapping("/registration")
    public String register(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult result, Model model) throws ParseException {
        System.out.println("Printing the user details while registering:"+userDTO);
        if(result.hasErrors()) {
            return "register";
        } else if(userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            userService.createUser(userDTO);
            model.addAttribute("successMsgRegistration", true);
            return "login";
        }
        return "register";
    }

    @GetMapping("/home")
    public String showHomePage(Model model) {
        return "home";
    }
}