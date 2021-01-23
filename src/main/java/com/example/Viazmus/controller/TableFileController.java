package com.example.Viazmus.controller;

import com.example.Viazmus.controller.searchLogic.ChangeIndex;
import com.example.Viazmus.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/listFile")
public class TableFileController {

    private String text;
    private String urlFile;

    @Value("${upload.path}")
    private String uploadPath;

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping
    public String getFile(@AuthenticationPrincipal User user, Model model)
    {
        boolean isChange = new NeedChangeIndexSingleton().getInstance().isChanged();
        model.addAttribute("status",isChange);
        model.addAttribute("adminName",user.getUsername());
        return "admin_table_file";
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping
    public String getListFile(@AuthenticationPrincipal User user, Model model)
    {

        ChangeIndex changeIndex = new ChangeIndex();
        changeIndex.reindex();
        boolean isChange = new NeedChangeIndexSingleton().getInstance().isChanged();

        model.addAttribute("status",isChange);
        model.addAttribute("adminName",user.getUsername());

        return "admin_table_file";
        /*
        ArrayList<String> myArrayList = new ArrayList<String>();
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            System.out.println("Файлов нет");
            return null;
        }

        File dir = new File(uploadDir.getAbsolutePath()); //path указывает на директорию
        File[] arrFiles = dir.listFiles();
        List<File> lst = Arrays.asList(arrFiles);

        for (int i=0;i<lst.size();i++)
        {
            myArrayList.add(lst.get(i).getName());
        }
        return myArrayList;*/
    }
}

