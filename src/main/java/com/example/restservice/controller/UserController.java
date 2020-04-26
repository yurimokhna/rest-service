package com.example.restservice.controller;

import com.example.restservice.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public User createNewUser (@RequestBody User user) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {

        user = User.createNewUser(user);
        return user;
    }
}
