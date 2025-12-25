package com.zsgs.busbooking.repositories;

import com.zsgs.busbooking.config.BeanFactory;
import com.zsgs.busbooking.model.Route;
import com.zsgs.busbooking.model.Trip;

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
        String sql = "SELECT route_id FROM route WHERE source = (?) AND destination = (?)";

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

    public Route findRouteById(String id ) throws SQLException{

        String sql = "SELECT * FROM route WHERE route_id = (?)";
        try(Connection connection = getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)
        ){
            Route route = BeanFactory.getInstance().createRoute();
            pstmt.setString(1,id);

            try( ResultSet rs = pstmt.executeQuery()){

                if( ! rs.next()){
                    return null ;
                }
                route.setRouteId(rs.getString("route_id"));
                route.setSource(rs.getString("source"));
                route.setDestination(rs.getString("destination"));
                route.setDistanceKm(rs.getInt("distance_km"));

                return route;
            }
        }

    }
}
