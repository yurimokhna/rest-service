package com.example.restservice.controller;

import com.example.restservice.model.Message;
import com.example.restservice.model.User;
import com.example.restservice.sql.SqlConnection;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        String sqlInsert = "INSERT INTO users (name , password) VALUES (?, ?)";

        PreparedStatement preparedStatement = MessageController.conn.prepareStatement(sqlInsert);
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, User.getHashPassword(user.getPassword()));
        preparedStatement.executeUpdate();

        Statement statement = MessageController.conn.createStatement();
        String sqlSelect = "SELECT name, password FROM users ORDER BY id DESC LIMIT 1";
        ResultSet rs = statement.executeQuery(sqlSelect);
        while (rs.next()) {
            user.setName(rs.getString(1));
            user.setPassword(rs.getString(2));
        }
        return user;
    }
}
