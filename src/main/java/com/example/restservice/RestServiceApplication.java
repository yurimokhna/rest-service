package com.example.restservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class RestServiceApplication {

	public static void main(String[] args) throws SQLException {

		SpringApplication.run(RestServiceApplication.class, args);

	}
}
