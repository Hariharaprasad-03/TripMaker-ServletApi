package com.zsgs.busbooking.util;

import com.zsgs.busbooking.repositories.BaseRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IdGeneratorUtil extends BaseRepository {


    String sql = """
                SELECT id 
                FROM id_generation
                where object = (?)
                """;
    String update = """
                UPDATE id_generation 
                SET id =(?)
                where object = (?)
                """;

    public  String generateId(String prefix ) throws SQLException {



        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1,prefix);

            try(ResultSet rs = pstmt.executeQuery()){

                if( ! rs.next()){
                    return  null ;
                }
                int suffix = rs.getInt("id" );
                updateId(prefix , suffix);
                return prefix + String.format("%03d",suffix);

            }
        }
    }

    public void updateId(String prefix , int suffix) throws SQLException {

        try(Connection connection = getConnection() ;
        PreparedStatement pstmt = connection.prepareStatement(update)){

            pstmt.setInt(1,suffix+1);
            pstmt.setString(2,prefix);

            pstmt.executeUpdate();
        }
    }
}
