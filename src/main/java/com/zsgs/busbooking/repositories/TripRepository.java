package com.zsgs.busbooking.repositories;

import com.zsgs.busbooking.enums.TripStatus;
import com.zsgs.busbooking.model.Trip;

import java.sql.*;

public class TripRepository  extends BaseRepository{

    String insert = "INSERT INTO trip (trip_id , route_id , bus_id ,start_time,end_time,status,date)values(?,?,?,?,?,?,?)";

    public boolean createTrip(Trip trip) throws SQLException {

        try(Connection connection = getConnection();
            PreparedStatement pstmt = connection.prepareStatement(insert)){

            pstmt.setString(1, trip.getTripId());
            pstmt.setString(2, trip.getRouteId());
            pstmt.setString(3, trip.getBusId());
            pstmt.setTime(4, Time.valueOf(trip.getStartTime()));
            pstmt.setTime(5,Time.valueOf(trip.getEndTime()));
            pstmt.setString(6, trip.getStaus().toString());
            pstmt.setDate(7, Date.valueOf(trip.getTripDate()));

            int row = pstmt.executeUpdate();
            return row > 0 ;
        }
    }

    public boolean cancelTrip(String tripId) throws SQLException{

        String sql = "UPDATE trip SET status = (?) where trip_id = (?)";

        try(Connection connection = getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){


            stmt.setString(1, TripStatus.FINISHED.toString());
            stmt.setString(2,tripId);

            int row = stmt.executeUpdate();

            return row >0 ;

        }
    }

    public boolean finishTrip(String tripId) throws SQLException{

        String sql = "UPDATE trip SET status = (?) where trip_id = (?)";

        try(Connection connection = getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){


            stmt.setString(1, TripStatus.CANCELLED.toString());
            stmt.setString(2,tripId);

            int row = stmt.executeUpdate();

            return row >0 ;

        }
    }



}
