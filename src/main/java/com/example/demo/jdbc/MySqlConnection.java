package com.example.demo.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySqlConnection {
	  public static void main(String[] args) {
		  	String jdbcUrl = "jdbc:mysql://yo-mysql.cn628cgo0r2b.ap-northeast-2.rds.amazonaws.com:3306/Board?serverTimezone=UTC&characterEncoding=UTF-8";
	        String username = "admin";
	        String password = "agape99^^";

	        try {
	            // MySQL JDBC 드라이버를 명시적으로 로드
	            Class.forName("com.mysql.cj.jdbc.Driver");

	            // 데이터베이스 연결
	            try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
	                System.out.println("MySQL 연결 성공!");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	    }
}
