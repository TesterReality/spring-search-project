package com.example.Viazmus.controller;


import com.example.Viazmus.controller.searchLogic.RaitingBoost;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/changeRating")
public class SearchJSRating {



    @PreAuthorize(value = "hasAuthority('ADMIN')or hasRole('USER')")
    @RequestMapping(method = RequestMethod.GET,
            produces = "application/json")
    public @ResponseBody
    double spinner(@RequestParam(name = "fileName",required = true, defaultValue = "none") String name) throws InterruptedException {

        RaitingBoost raitingBoost = new RaitingBoost();
        raitingBoost.boostDoc(name,"0.25");

        System.out.println("Файл "+name +" + 0.25 рейтинга");
        double fileRating = raitingBoost.getBoostRaiting(name);

        return fileRating;
    }


}
