package com.yapp.fmz.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfraController {

    @GetMapping("/healthcheck")
    public String home(){
        return "good";
    }
}
