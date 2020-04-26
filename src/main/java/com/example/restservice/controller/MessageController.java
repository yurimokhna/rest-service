package com.example.restservice.controller;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import com.example.restservice.model.Message;
import com.example.restservice.model.MessageCreateRequest;
import com.example.restservice.model.MessagesSearchResponse;
import com.example.restservice.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageController {

    @GetMapping("/search")
    public MessagesSearchResponse getSearchMessages(@RequestParam(value = "latitude", required = false) Double latitude,
                                                    @RequestParam(value = "longitude", required = false) Double longitude,
                                                    @RequestParam(value = "radius", required = false) Double radius,
                                                    @RequestHeader("user_name") String userName,
                                                    @RequestHeader("user_password") String userPassword)
                                                    throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {

        User.isValidUser(userName, userPassword);
        return Message.getMessagesSearchResponse(latitude, longitude, radius);
    }

    @GetMapping("/getAll")
    public MessagesSearchResponse getMessages(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                              @RequestParam(value = "messageOnPage", required = false, defaultValue = "5")
                                              int messageOnPage,
                                              @RequestHeader("user_name") String userName,
                                              @RequestHeader("user_password") String userPassword)
                                              throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {

        User.isValidUser(userName, userPassword);
        return Message.getMessagesAllResponse(page, messageOnPage);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Message MessageCreateRequest(@RequestBody MessageCreateRequest request,
                                        @RequestHeader("user_name") String userName,
                                        @RequestHeader("user_password") String userPassword)
                                        throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {

        User.isValidUser(userName, userPassword);
        return Message.createNewMessage(request);
    }
}
