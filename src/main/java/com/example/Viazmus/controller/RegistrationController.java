package com.example.Viazmus.controller;

import com.example.Viazmus.domain.RegistrationForm;
import com.example.Viazmus.domain.Role;
import com.example.Viazmus.domain.User;
import com.example.Viazmus.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public String registration()
    {
        return "register";
    }

    @PostMapping
    public String addUser(User user, Model model, RegistrationForm registrationForm)
    {
       User userFromDB= userRepo.findByUsername(user.getUsername());
       if(userFromDB != null)
       {
           model.addAttribute("message",true);
           return "register";
       }
       user.setRoles(Collections.singleton(Role.USER));
       userRepo.save(registrationForm.toUser(passwordEncoder));
        return "redirect:/login";
    }
}

