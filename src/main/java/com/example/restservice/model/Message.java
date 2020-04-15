package com.example.restservice.model;

import com.example.restservice.controller.MessageController;
import com.example.restservice.sql.SqlConnection;
import com.example.restservice.sql.SqlNames;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.*;
import java.util.ArrayList;

public class Message {
    private String text;
    private String userName;
    private double xCoordinate;
    private double yCoordinate;
    private Timestamp createDate;

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

    public void setCreateDate(Timestamp createDate) {
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

    public Timestamp getCreateDate() {
        return this.createDate;
    }

    public static MessagesSearchResponse getMessagesSearchResponse (Double latitude, Double longitude, Double radius) throws SQLException, ClassNotFoundException {
        if(latitude == null){
            throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Incorrect latitude");
        }
        else if(longitude == null){
            throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Incorrect longitude");
        }
        else if(radius == null){
            throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Incorrect radius");
        }

        double latitudeMin = latitude - (radius * 9.0 * 0.000001);
        double latitudeMax = latitude + (radius * 9.0 * 0.000001);
        double longitudeMin = longitude - (radius * 12.0 * 0.000001);
        double longitudeMax = longitude + (radius * 12.0 * 0.000001);

        ArrayList<Message> messagesSearch = new ArrayList<Message>();
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
            message.setCreateDate(rs.getTimestamp(5));
            messagesSearch.add(message);
        }
        messagesSearchResponse.setMessages(messagesSearch);
        return messagesSearchResponse;
    }

    public static MessagesSearchResponse getMessagesAllResponse (int page, int messageOnPage)
            throws SQLException, ClassNotFoundException {

        ArrayList<Message> messagesOnPage = new ArrayList<>();
        MessagesSearchResponse messagesSearchResponse = new MessagesSearchResponse();

        if(MessageController.conn == null) MessageController.conn = SqlConnection.getMySQLConnection();

        int countMessage = 0;
        String sqlSelectCount = "SELECT COUNT(*) FROM messages";
        PreparedStatement preparedStatement = MessageController.conn.prepareStatement(sqlSelectCount);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            countMessage = rs.getInt(1);
        }

        int countPage = (int) Math.ceil((double)countMessage / (double) messageOnPage);
        if (page > countPage) throw new ResponseStatusException( HttpStatus.NOT_FOUND, "Incorrect page");

        String sqlSelectOnPage = "SELECT text, user_name, X( `coordinate` ) , Y( `coordinate` ), " +
                " create_date FROM messages.messages ORDER BY create_date DESC LIMIT ? offset ?";

        preparedStatement = MessageController.conn.prepareStatement(sqlSelectOnPage);
        preparedStatement.setInt(1, messageOnPage);
        preparedStatement.setInt(2, ((page * messageOnPage) - messageOnPage));

        rs =  preparedStatement.executeQuery();
        while (rs.next()) {
            Message message = new Message();
            message.setText(rs.getString(1));
            message.setUserName(rs.getString(2));
            message.setxCoordinate(rs.getDouble(3));
            message.setyCoordinate(rs.getDouble(4));
            message.setCreateDate(rs.getTimestamp(5));
            messagesOnPage.add(message);
        }
        messagesSearchResponse.setMessages(messagesOnPage);
        messagesSearchResponse.setCountMessage(countMessage);
        messagesSearchResponse.setPage(page);
        messagesSearchResponse.setCountPage(countPage);

        return messagesSearchResponse;
    }
}
