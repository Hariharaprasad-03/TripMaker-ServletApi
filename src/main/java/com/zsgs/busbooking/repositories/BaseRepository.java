package com.zsgs.busbooking.repositories;

import com.zsgs.busbooking.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseRepository {

    private DatabaseConfig databaseConfig ;

    public BaseRepository(){
        databaseConfig = DatabaseConfig.getInstance();
    }

    protected Connection getConnection() throws SQLException {
        return databaseConfig.getConnection();
    }

    protected void closeResourses(Connection connection , PreparedStatement pstmt , ResultSet rs){
        try{
            if(connection != null ) connection.close();
            if(pstmt != null) pstmt.close();
            if(rs != null ) rs.close();
        }
        catch (SQLException e){

            System.out.println(e.getMessage());
        }
    }

    public void closeResourses(Connection connection , PreparedStatement pstmt ){
        closeResourses(connection,pstmt,null);
    }
}
