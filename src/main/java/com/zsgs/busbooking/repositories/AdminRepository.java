package com.zsgs.busbooking.repositories;

import com.zsgs.busbooking.model.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminRepository extends  BaseRepository{

    String insertQuery = "INSERT INTO admin (admin_id , admin_name ,password) values(?,?,?)";
    public boolean addAdmin(Admin admin)  throws SQLException {

        try(Connection connection = getConnection();
            PreparedStatement pstmt = connection.prepareStatement(insertQuery)){

            pstmt.setString(1,admin.getAdminId());
            pstmt.setString(2, admin.getAdminName());
            pstmt.setString(3, admin.getPassword());

            int row = pstmt.executeUpdate();
            return row > 0 ;
        }

    }
}
