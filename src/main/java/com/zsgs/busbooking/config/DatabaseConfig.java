package com.zsgs.busbooking.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {


    private static DatabaseConfig instance;


    private final String DB_URL;
    private final String DB_USER;
    private final String DB_PASSWORD;
    private final String DB_DRIVER;


    private DatabaseConfig() {

        this.DB_URL = "jdbc:mysql://localhost:3306/busbooking";
        this.DB_USER = "root";
        this.DB_PASSWORD = "Hariprasad!03";
        this.DB_DRIVER = "com.mysql.cj.jdbc.Driver";

        try {
            Class.forName(DB_DRIVER);
            System.out.println(" Sql is Connected Well ");
        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }

    public static DatabaseConfig getInstance() {

        if (instance ==  null){

            instance = new DatabaseConfig();
        }
        return instance ;
    }

    public Connection getConnection() throws SQLException {

        return DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
    }


    public String getDbUrl() {
        return DB_URL;
    }

    public String getDbUser() {
        return DB_USER;
    }



}
