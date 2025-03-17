package com.catalin.eventmanager.controller;

import com.catalin.eventmanager.documents.User;
import com.catalin.eventmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/info")
    public ResponseEntity<User> getInfo(@RequestParam String username) {
        Optional<User> user = userService.getUserInfo(username);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user.get());
    }

}
