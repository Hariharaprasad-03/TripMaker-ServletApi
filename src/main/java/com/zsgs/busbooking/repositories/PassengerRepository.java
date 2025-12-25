package com.zsgs.busbooking.repositories;

import com.zsgs.busbooking.config.BeanFactory;
import com.zsgs.busbooking.model.Passenger;

import java.sql.*;

public class PassengerRepository extends BaseRepository{


    public boolean addPassenger(Passenger passenger) throws SQLException {

        String sql = "INSERT INTO passenger(passenger_id,passenger_name,password,email,mobile_number)values(?,?,?,?,?)";

        try(Connection connection = getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);) {

            pstmt.setString(1, passenger.getPassengerId());
            pstmt.setString(2, passenger.getPassengerName());
            pstmt.setString(3, passenger.getPassword());
            pstmt.setString(4, passenger.getEmail());
            pstmt.setString(5, passenger.getMobileNumber());

            int row = pstmt.executeUpdate();
            return row > 0;
        }

    }

    public Passenger findPassengerById(String id) throws SQLException {
        String sql = "SELECT * FROM passenger WHERE passenger_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, id);

            try(ResultSet rs = pstmt.executeQuery()) {


                if (rs.next()) {
                    Passenger passenger = new Passenger();
                    passenger.setPassengerId(rs.getString("passenger_id"));
                    passenger.setPassengerName(rs.getString("passenger_name"));
                    passenger.setMobileNumber(rs.getString("mobile_number"));
                    passenger.setEmail(rs.getString("email"));
                    passenger.setPassword(rs.getString("password"));
                    return passenger;
                }
            }
        }

        return null;
    }

    public Passenger findPassenerByEmail(String email) throws SQLException {

        String sql = "SELECT * FROM passenger WHERE email = ?";

        try( Connection connection = getConnection() ;
        PreparedStatement pstmt = connection.prepareStatement(sql))  {

            pstmt.setString(1, email);
            try(ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    Passenger passenger = new Passenger();
                    passenger.setPassengerId(rs.getString("passenger_id"));
                    passenger.setPassengerName(rs.getString("passenger_name"));
                    passenger.setMobileNumber(rs.getString("mobile_number"));
                    passenger.setEmail(rs.getString("email"));
                    passenger.setPassword(rs.getString("password"));
                    return passenger;
                }
            }
        }

        return null;
    }


    public boolean ifEmailAlreadyExist(String email) throws SQLException {

        String sql = "SELECT COUNT(*) FROM passenger WHERE email =(?)";

        try(  Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql) ) {
            connection.close();
            pstmt.setString(1,email);
            try(ResultSet rs = pstmt.executeQuery()) {
                int count = 0;

                if (rs.next()) {
                    count = rs.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }


    public boolean ifMobileNumberAlreadyExist(String mobile) throws SQLException {
        String sql = "SELECT COUNT(*) FROM passenger WHERE mobile_number =(?)";
        try(Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1,mobile);
            try(ResultSet rs = pstmt.executeQuery()) {

                int count = 0;
                if (rs.next()) {
                    count = rs.getInt(1);

                }
                return count > 0;
            }
        }


    }

    public Passenger findPassengerByMobileNumber(String mobileNumer) throws SQLException {

        String sql = """
                SELECT *
                FROM passenger 
                WHERE mobile_number = (?)
                """;

        try(Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql)){

            pstmt.setString(1,mobileNumer);

            try( ResultSet rs = pstmt.executeQuery()){

                if( ! rs.next()){

                    return null;
                }
                Passenger passenger = BeanFactory.getInstance().createPassanger();
                passenger.setPassengerId(rs.getString("passenger_id"));
                passenger.setEmail(rs.getString("email"));
                passenger.setMobileNumber(rs.getString("mobile_number"));
                passenger.setPassengerName(rs.getString("passenger_name"));
                passenger.setPassword(rs.getString("password"));

                return passenger;
            }
        }
    }


}
