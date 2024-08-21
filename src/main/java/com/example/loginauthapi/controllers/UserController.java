package com.example.loginauthapi.controllers;

import com.example.loginauthapi.Repositories.UserRepository;
import com.example.loginauthapi.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<String> getUser(){
        return ResponseEntity.ok("sucesso!");
    }

//    @GetMapping
//    public ResponseEntity<List<User>> getAllUsers() {
//        List<User> users = this.repository.findAll();
//        return ResponseEntity.ok(users);
//    }
}