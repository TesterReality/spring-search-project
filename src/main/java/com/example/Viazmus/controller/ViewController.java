package com.example.Viazmus.controller;

import com.example.Viazmus.domain.RegistrationForm;
import com.example.Viazmus.domain.Role;
import com.example.Viazmus.domain.User;
import com.example.Viazmus.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

@Controller
@RequestMapping("/view")
public class ViewController {

    private String text;
    private String urlFile;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping
    public String getFile(@RequestParam (name = "file",required = true, defaultValue = "none") String name,Model model)
    {

        File dirUpload = new File(uploadPath);
        File f = new File(dirUpload.getAbsolutePath()+"/"+name);
        String contents="";

        if(f.exists() && f.isFile()) {

            try {
                contents = readUsingFiles(f.getAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        model.addAttribute("fileText",contents);
        return "view";
    }


    private static String readUsingFiles(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
}

