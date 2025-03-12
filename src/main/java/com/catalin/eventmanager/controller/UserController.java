package com.catalin.eventmanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/user/info")
    public HttpStatus getInfo (){
        return HttpStatus.OK;
    }
}
