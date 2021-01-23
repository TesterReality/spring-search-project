package com.example.Viazmus.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/upload")
public class FileUploadController {

    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {

        if(!file.isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
                System.out.println(uploadDir.getAbsolutePath());
            }

            DateFormat dateFormat = new SimpleDateFormat("(dd-MM-yyyy)(HH-mm-ss)");
            Date date = new Date();
            String strDate = dateFormat.format(date);

            strDate = strDate + "-" + file.getOriginalFilename();

            file.transferTo(new File(uploadDir.getAbsolutePath() + "/" + strDate));
            System.out.println("uploaded file " + uploadDir.getAbsolutePath() + "/" + strDate);

            new NeedChangeIndexSingleton().getInstance().setChanged(true);


            return "upload";
        }else
        {
            return "download";
        }
    }

}