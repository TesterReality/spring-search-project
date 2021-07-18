package com.example.Viazmus.controller;


import com.example.Viazmus.controller.searchLogic.EnLemmatizer;
import com.example.Viazmus.controller.searchLogic.Lemmatizer;
import com.example.Viazmus.controller.searchLogic.Search;
import com.example.Viazmus.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    @GetMapping
    public String getFile(@RequestParam(name = "q",required = true, defaultValue = "none") String qeuery,
                          @RequestParam(name = "p",required = true, defaultValue = "1") String page,
                          @AuthenticationPrincipal User user,
                          Model model)
    {

        if(user != null)
        {
            model.addAttribute("name",user.getUsername());
        }else
        {
            model.addAttribute("name","Пользователь!");
        }

        String[] word = qeuery.split(" ");
        Lemmatizer lemmatizer = null;

        try {
             lemmatizer = new EnLemmatizer();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

        for(int i=0;i<word.length;i++)
        {
            word[i]=lemmatizer.getLemma(word[i]);
        }
        Search search = new Search();
        List<ResultSearch> resultSearches = new ArrayList<>();
        try {

            resultSearches= search.startSearch(word);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<ResultSearch> fixResultSearches = new ArrayList<>();
        if(resultSearches!=null) {
            for (int i = 0; i < resultSearches.size(); i++) {
                if (!resultSearches.get(i).getSecondWord().equals(" ...")) {
                    fixResultSearches.add(resultSearches.get(i));
                }
            }
        }
        model.addAttribute("result",fixResultSearches);
      //  model.addAttribute("fileName",name);

        return "search";
    }
}
