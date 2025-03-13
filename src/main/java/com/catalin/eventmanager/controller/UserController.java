package com.catalin.eventmanager.controller;

import com.catalin.eventmanager.dto.request.UserInformationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @GetMapping("/user/info")
    public ResponseEntity<String> getInfo(@RequestParam(required = false) String username) {
        if (username == null || username.isEmpty()) {
            return new ResponseEntity<>("Invalid body", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("User info", HttpStatus.OK);
    }
}

