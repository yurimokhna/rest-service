package com.example.restservice.model;

import com.example.restservice.controller.MessageController;
import com.example.restservice.sql.SqlConnection;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "name='" + name + '\'' +
                ", password='" + password + '\'';
    }

    public static boolean isValidUser(String userName, String userPassword) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException {
        if (MessageController.conn == null) MessageController.conn = SqlConnection.getMySQLConnection();

        User user = new User();

        String SqlSelectUser = "SELECT name, password FROM  users where name= ?";

        PreparedStatement preparedStatement = MessageController.conn.prepareStatement(SqlSelectUser);
        preparedStatement.setString(1, userName);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            user.setName(rs.getString(1));
            user.setPassword(rs.getString(2));
        }
        String lineSeparator = System.getProperty("line.separator");
        if (user.getName() == null) {
            System.out.println(lineSeparator +"Пользователь не найден в БД");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect user name");
        }
        String encodedPassword = User.getHashPassword(userPassword);
        if (user.getPassword().equals(encodedPassword)) {
            System.out.println(lineSeparator + "Найденный пользователь в БД - " + user);
            System.out.println("Введенные данные            - name='" + userName +
                    "', password='" + encodedPassword + "'" + " passwordUnencrypted='" + userPassword + "'");
            System.out.println("login successful");
            return true;
        } else {
            System.out.println(lineSeparator + "Найденный пользователь в БД - " + user);
            System.out.println("Введенные данные            - name='" + userName +
                    "', password='" + encodedPassword + "'" + " passwordUnencrypted='" + userPassword + "'");
            System.out.println("login failed");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect password");
        }
    }

    public static String getHashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] encodedByte = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));

        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < encodedByte.length; i++) {
            String hex = Integer.toHexString(0xff & encodedByte[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
