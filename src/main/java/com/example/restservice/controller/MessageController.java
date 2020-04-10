package com.example.restservice.controller;

import java.sql.*;

import com.example.restservice.model.Message;
import com.example.restservice.model.MessageCreateRequest;
import com.example.restservice.model.MessagesSearchResponse;
import com.example.restservice.model.User;
import com.example.restservice.sql.SqlConnection;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
public class MessageController {
    public static Connection conn;

    public MessageController() {
    }

    @GetMapping("/searchMessages")
    public MessagesSearchResponse getSearchMessages(@RequestParam(value = "latitude", required = false) Double latitude,
                                              @RequestParam(value = "longitude", required = false) Double longitude,
                                              @RequestParam(value = "radius", required = false) Double radius,
                                              @RequestHeader ("userName") String userName,
                                              @RequestHeader("userPassword") String userPassword)
                                              throws SQLException, ClassNotFoundException {

        User.isValidUser(userName, userPassword);
        return Message.getMessagesSearchResponse(latitude, longitude, radius);
    }

    @GetMapping("/allMessages")
    public MessagesSearchResponse getMessages(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                              @RequestParam(value = "messageOnPage", required = false, defaultValue = "5")
                                              int messageOnPage,
                                              @RequestHeader ("userName") String userName,
                                              @RequestHeader("userPassword") String userPassword)
                                              throws SQLException, ClassNotFoundException {

        User.isValidUser(userName, userPassword);
        return Message.getMessagesAllResponse(page, messageOnPage);
    }

    @PostMapping("/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public Message MessageCreateRequest(@RequestBody MessageCreateRequest request,
                                        @RequestHeader ("userName") String userName,
                                        @RequestHeader("userPassword") String userPassword)
                                        throws SQLException, ClassNotFoundException {

        if(conn == null) conn = SqlConnection.getMySQLConnection();
        User.isValidUser(userName, userPassword);
        String sqlInsert = "INSERT INTO `messages`.`messages`(`text`,`user_name`,`coordinate`,`create_date`)" +
                           "VALUES(?,?,(GeomFromWKB(Point(?,?))),now());";

        PreparedStatement preparedStatement = conn.prepareStatement(sqlInsert);
        preparedStatement.setString(1, request.getText());
        preparedStatement.setString(2, request.getUser_name());
        preparedStatement.setString(3, request.getLatitude());
        preparedStatement.setString(4, request.getLongitude());
        preparedStatement.executeUpdate();

        Statement statement = conn.createStatement();
        String sqlSelect = "SELECT text, user_name, X( `coordinate` ) , Y( `coordinate` ), create_date" +
                           " FROM messages.messages ORDER BY create_date DESC LIMIT 1";
        ResultSet rs = statement.executeQuery(sqlSelect);
        Message message = new Message();
        while (rs.next()) {
            message.setText(rs.getString(1));
            message.setUserName(rs.getString(2));
            message.setxCoordinate(rs.getDouble(3));
            message.setyCoordinate(rs.getDouble(4));
            message.setCreateDate(rs.getDate(5));
        }
        return message;
    }

    @PostMapping("/createUser")
    public User createNewUser (@RequestBody User user) throws SQLException, ClassNotFoundException {
        if(conn == null) conn = SqlConnection.getMySQLConnection();
        String sqlInsert = "INSERT INTO users (name , password) VALUES (?, ?)";

        PreparedStatement preparedStatement = conn.prepareStatement(sqlInsert);
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.executeUpdate();

        Statement statement = conn.createStatement();
        String sqlSelect = "SELECT name, password FROM users ORDER BY id DESC LIMIT 1";
        ResultSet rs = statement.executeQuery(sqlSelect);
        while (rs.next()) {
            user.setName(rs.getString(1));
            user.setPassword(rs.getString(2));
        }
        return user;
    }
}
