package com.example.Viazmus.controller;
import com.example.Viazmus.controller.searchLogic.ChangeIndex;
import com.example.Viazmus.controller.searchLogic.RaitingBoost;
import com.example.Viazmus.domain.HistorySearch;
import com.example.Viazmus.domain.User;
import com.example.Viazmus.repos.HistorySearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private HistorySearchRepository historySearchRepository;


    @Value("${upload.path}")
    private String uploadPath;

    //AuthenticationPrincipal - достает из куки нужного пользователя если есть
    @GetMapping("/")
    public String main(@AuthenticationPrincipal User user,Model model)
    {
        if(user!=null)
        {
            model.addAttribute("user",user.getUsername());
            return "main";
        }

        model.addAttribute("anon","Пользователь");
        return "main";
    }

    @GetMapping("/login")
    public String login()
    {
        return "login";
    }

    @PreAuthorize(value = "hasAuthority('ADMIN') or hasAuthority('USER')")

    @GetMapping("/foruser")
    public String forUser(@AuthenticationPrincipal User user,
                          Model model)
    {
        model.addAttribute("adminName",user.getUsername());
        return "user_table_file";
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping("/admin/uploadFile")
    public String forAdmin( @AuthenticationPrincipal User user,
                                       Model model)
    {
        boolean isChange = new NeedChangeIndexSingleton().getInstance().isChanged();
        model.addAttribute("status",isChange);


        model.addAttribute("adminName",user.getUsername());
        return "admin";
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping("/admin/uploadFile")
    public String newindex( @AuthenticationPrincipal User user,
                            Model model)
    {

        ChangeIndex changeIndex = new ChangeIndex();
        changeIndex.reindex();
        boolean isChange = new NeedChangeIndexSingleton().getInstance().isChanged();


        model.addAttribute("status",isChange);

        model.addAttribute("adminName",user.getUsername());
        return "admin";
    }


    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping("/editFile")
    public String getFile(@RequestParam (name = "file",required = true, defaultValue = "none") String name,
                          @AuthenticationPrincipal User user,
                          Model model)
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
        model.addAttribute("adminName",user.getUsername());
        model.addAttribute("fileText",contents);
        model.addAttribute("fileName",name);

        return "file_edit";
    }


    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping("/saveFile")
    public String saveFile(@RequestParam (name = "names",required = true, defaultValue = "none") String names,
                           @RequestParam (name = "fileText",required = true, defaultValue = "none") String fileText
                         )
    {
        System.out.println("Сохранили файл");




        if(names.equals("none") || fileText.equals("none"))
        {
            return "admin_table_file";
        }

        File dirUpload = new File(uploadPath);
        File f = new File(dirUpload.getAbsolutePath()+"/"+names);


        FileWriter fooWriter = null; // true to append
        try {
            fooWriter = new FileWriter(f, false);
            // false to overwrite.
            fooWriter.write(fileText);
            fooWriter.close();

           new NeedChangeIndexSingleton().getInstance().setChanged(true);
        } catch (IOException e) {
            System.out.println("Ошибка в файле MainController, method saveFile");
            return "admin_table_file";
        }


        return "admin_table_file";
    }

    private static String readUsingFiles(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    @PostMapping("/")
    public String add(@AuthenticationPrincipal User user,
            @RequestParam String text, Model model)
    {
        HistorySearch historySearch;
        if(user!=null)
        {
            historySearch= new HistorySearch(text,user);
            historySearchRepository.save(historySearch);
        }
        Iterable<HistorySearch> historysearchs= historySearchRepository.findAll();

        String[] word = text.split(" ");
        String result ="";
        for(int i=0;i<word.length;i++)
        {
            if(!word[i].isEmpty())
            {
                if(result.equals(""))
                {
                    result = word[i];
                }else
                result= result +"+"+word[i] ;

            }
        }


        model.addAttribute("historysearchs",historysearchs);


        return "redirect:/search?q="+result;
    }

}