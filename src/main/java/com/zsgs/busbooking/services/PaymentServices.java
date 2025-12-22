package com.zsgs.busbooking.services;

import com.zsgs.busbooking.model.Payment;
import com.zsgs.busbooking.repositories.PaymentRepository;
import com.zsgs.busbooking.util.IdGenerator;

import java.sql.Connection;
import java.sql.SQLException;

public class PaymentServices {

    private final PaymentRepository paymentRepository;
    private static long id =1 ;

    PaymentServices(PaymentRepository paymentRepository){
        this.paymentRepository = paymentRepository;
    }

    public void pay(Payment payment)throws SQLException {
        payment.setPaymentId( new IdGenerator().genarateId("PAY",id++));
     paymentRepository.pay(payment);
    }

    public void pay(Connection connection , Payment payment) throws SQLException{
        payment.setPaymentId( new IdGenerator().genarateId("PAY",id++));
        paymentRepository.pay(connection,payment);
    }
}
