package com.example.restservice.model;

import com.example.restservice.controller.MessageController;
import com.example.restservice.properties.SqlConnection;
import com.example.restservice.properties.SqlNames;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class Message {
    private String text;
    private String userName;
    private double xCoordinate;
    private double yCoordinate;
    private Date createDate;

    public void setText(String text) {
        this.text = text;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setxCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public void setyCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getText() {
        return this.text;
    }

    public String getUserName() {
        return this.userName;
    }

    public double getxCoordinate() {
        return this.xCoordinate;
    }

    public double getyCoordinate() {
        return this.yCoordinate;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public static MessagesSearchResponse getMessagesSearchResponse (Double latitude, Double longitude, Double radius) throws SQLException, ClassNotFoundException {
        if(latitude == null) throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Incorrect latitude");
        else if(longitude == null) throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Incorrect longitude");
        else if(radius == null) throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Incorrect radius");
        double latitudeMin = latitude - (radius * 9.0 * 0.000001);
        double latitudeMax = latitude + (radius * 9.0 * 0.000001);
        double longitudeMin = longitude - (radius * 12.0 * 0.000001);
        double longitudeMax = longitude + (radius * 12.0 * 0.000001);

        ArrayList<Message> messages = new ArrayList<Message>();
        MessagesSearchResponse messagesSearchResponse = new MessagesSearchResponse();
        if(MessageController.conn == null) MessageController.conn = SqlConnection.getMySQLConnection();
        String sqlSelect = "SELECT " + SqlNames.TEXT + ", " +  SqlNames.USER + ", " +
                SqlNames.COORDINATE_X + ", " + SqlNames.COORDINATE_Y + "," +
                " " + SqlNames.DATE + " FROM " + SqlNames.DB + "." + SqlNames.TABLE +
                " where " + SqlNames.COORDINATE_X + " between ? and  ? and " +
                SqlNames.COORDINATE_Y + " between ? and ? ";

        PreparedStatement preparedStatement = MessageController.conn.prepareStatement(sqlSelect);
        preparedStatement.setString(1, String.valueOf(latitudeMin));
        preparedStatement.setString(2, String.valueOf(latitudeMax));
        preparedStatement.setString(3, String.valueOf(longitudeMin));
        preparedStatement.setString(4, String.valueOf(longitudeMax));
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            Message message = new Message();
            message.setText(rs.getString(1));
            message.setUserName(rs.getString(2));
            message.setxCoordinate(rs.getDouble(3));
            message.setyCoordinate(rs.getDouble(4));
            message.setCreateDate(rs.getDate(5));
            messages.add(message);
        }
        messagesSearchResponse.setMessages(messages);
        return messagesSearchResponse;
    }
}
