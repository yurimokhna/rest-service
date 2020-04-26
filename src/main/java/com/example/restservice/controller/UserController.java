package com.example.restservice.controller;

import com.example.restservice.model.User;
import com.example.restservice.sql.SqlConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/create")
    public User createNewUser (@RequestBody User user) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
        if(MessageController.conn == null) MessageController.conn = SqlConnection.getMySQLConnection();
        user = User.createNewUser(user);
        return user;
    }
}
