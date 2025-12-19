package com.zsgs.busbooking.repositories;

import com.zsgs.busbooking.model.Route;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RouteRepository extends BaseRepository{

    public boolean addRoute(Route route) throws SQLException {

        String sql ="INSERT INTO route (route_id,source,destination,distance_km) values(?,?,?,?)";

        try(Connection connection = getConnection() ;
            PreparedStatement pstmt = connection.prepareStatement(sql)){

            pstmt.setString(1,route.getRouteId());
            pstmt.setString(2,route.getSource());
            pstmt.setString(3,route.getDestination());
            pstmt.setInt(4,route.getDistanceKm());
            int row  = pstmt.executeUpdate();

            return row >0 ;
        }
    }

    public String findRouteIdBySourceAndDestination(String source, String destination) throws SQLException {
        String sql = "SELECT route_id FROM route WHERE source = ? AND destination = ?";

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, source);
            pstmt.setString(2, destination);


            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("route_id");
                }
            }
        }
        return null;
    }
}
