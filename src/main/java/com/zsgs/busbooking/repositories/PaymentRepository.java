package com.zsgs.busbooking.repositories;

import com.zsgs.busbooking.model.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PaymentRepository  extends BaseRepository {


    public boolean pay(Payment payment)throws SQLException {

        String sql = """
               INSERT INTO payment (payment_id,payment_type,amount,payment_status,payment_address,passenger_id)
               values(?,?,?,?,?,?)
                """;

        try(Connection connection = getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){

            pstmt.setString(1, payment.getPaymentId());
            pstmt.setString(2,"UPI");
            pstmt.setDouble(3,payment.getAmount());
            pstmt.setString(4,"CONFIRMED");
            pstmt.setString(5, payment.getPaymentAddress());
            pstmt.setString(6, payment.getPassengerId());

            return pstmt.executeUpdate() >0;
        }
    }
    public boolean pay(Connection connection ,Payment payment)throws SQLException {

        String sql = """
               INSERT INTO payment (payment_id,payment_type,amount,payment_status,payment_address,passenger_id)
               values(?,?,?,?,?,?)
                """;

        try(
            PreparedStatement pstmt = connection.prepareStatement(sql)){

            pstmt.setString(1, payment.getPaymentId());
            pstmt.setString(2,"UPI");
            pstmt.setDouble(3,payment.getAmount());
            pstmt.setString(4,"CONFIRMED");
            pstmt.setString(5, payment.getPaymentAddress());
            pstmt.setString(6, payment.getPassengerId());

            return pstmt.executeUpdate() >0;
        }
    }


}
