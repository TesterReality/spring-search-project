package com.example.Viazmus.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/getfile")
public class HomeController {

    @Value("${upload.path}")
    private String uploadPath;

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @RequestMapping(method = RequestMethod.GET,
            produces = "application/json")
    public @ResponseBody Answer spinner(@RequestParam (name = "text",required = true, defaultValue = "none") String name,Model model) throws InterruptedException {

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            System.out.println("Файлов нет");
            return null;
        }

        File dir = new File(uploadDir.getAbsolutePath()); //path указывает на директорию
        File[] arrFiles = dir.listFiles();
        List<File> lst = new ArrayList<>();
        for(int i=0;i<arrFiles.length; i++)
        {
            if(arrFiles[i].isFile())
            {
                lst.add(arrFiles[i]);
            }
        }


        String[] resultArrayFile = new String[lst.size()];

        for (int i=0;i<lst.size();i++)
        {

            resultArrayFile[i]=lst.get(i).getName();
        }


        return new Answer(resultArrayFile);
    }


    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @RequestMapping(value = "/edit",method = RequestMethod.GET,
            produces = "application/json")
    public @ResponseBody Answer fileText(@RequestParam (name = "fileName",required = true, defaultValue = "none") String name,Model model) throws InterruptedException {

        String answerStr = "/editFile?file="+name;
        return new Answer(answerStr);
    }


    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @RequestMapping(value = "/del",method = RequestMethod.GET,
            produces = "application/json")
    public @ResponseBody Answer delFile(@RequestParam (name = "fileName",required = true, defaultValue = "none") String name,Model model) throws InterruptedException {


        File dirUpload = new File(uploadPath);

        try {
            File f = new File(dirUpload.getCanonicalFile()+"/"+name);
            if(f.delete()){
                new NeedChangeIndexSingleton().getInstance().setChanged(true);

                return new Answer("Файл успешно удален");
            }else
                return new Answer("Файл не найден и соответственно не удален");

        } catch (IOException e) {
            return new Answer("Во время удаления файла что то пошло не так");
        }
    }


}