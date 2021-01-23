package com.example.Viazmus.controller;


import com.example.Viazmus.controller.searchLogic.RaitingBoost;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/exit")
public class LogoutAjax {

    @PreAuthorize(value = "hasAuthority('ADMIN')or hasRole('USER')")
    @RequestMapping(method = RequestMethod.GET,
            produces = "application/json")
    public @ResponseBody
    String spinner() throws InterruptedException {


        return "/logout";
    }
}
