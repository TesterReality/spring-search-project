package com.example.Viazmus.domain;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Data
public class RegistrationForm {


    private String username;
    private String pswd;

    public User toUser(PasswordEncoder passwordEncoder)
    {
        User user = new User();
        user.setPswd(passwordEncoder.encode(pswd));
        user.setUsername(username);
        user.setRoles(Collections.singleton(Role.USER));
        return user;
    }
}
