package com.zsgs.busbooking;

import com.zsgs.busbooking.payloads.PassengerSignUpRequest;
import com.zsgs.busbooking.repositories.BusRepository;
import com.zsgs.busbooking.repositories.PassengerRepository;
import com.zsgs.busbooking.services.BusService;
import com.zsgs.busbooking.services.PassengerService;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;

public class BusBooking {

    public static  void main(String[] args){

        try {
            PassengerService passengerService = new PassengerService( new PassengerRepository());
            BusService busService = new BusService(new BusRepository());

            System.out.println(LocalDate.now());

            PassengerSignUpRequest p1 = new PassengerSignUpRequest();
            p1.setEmail("test@gmail.com");
            p1.setMobileNumber("9111111111");
            p1.setPassword("a");
            p1.setPassengerName("A");

            passengerService.addPassenger(p1);

            PassengerSignUpRequest p2 = new PassengerSignUpRequest();
            p2.setEmail("test2@gmail.com");
            p2.setPassword("b");
            p2.setPassengerName("B");
            p2.setMobileNumber("9323232323");

            passengerService.addPassenger(p2);



        } catch (SQLIntegrityConstraintViolationException e) {

            if (e.getErrorCode() == 1062) {

                String msg = e.getMessage();
                System.out.println("Full error: " + msg);


                if (msg.contains("uk_passenger_email")) {
                    String duplicateEmail = extractDuplicateValue(msg);
                    System.out.println(" Email already exists: " + duplicateEmail);

                } else if (msg.contains("uk_passenger_mobile")) {
                    String duplicateMobile = extractDuplicateValue(msg);
                    System.out.println(" Mobile number already exists: " + duplicateMobile);

                } else if (msg.contains("PRIMARY")) {
                    String duplicateId = extractDuplicateValue(msg);
                    System.out.println(" Passenger ID already exists: " + duplicateId);

                } else {
                    System.out.println("Duplicate value detected");
                }
            }
            System.out.println(e.getMessage());
        }
        catch ( SQLException e) {

            e.printStackTrace();
        }
        catch (Exception e){

            System.out.println(e.getMessage());
        }

    }

    private static String extractDuplicateValue(String errorMessage) {
        try {
            int startIndex = errorMessage.indexOf("'") + 1;
            int endIndex = errorMessage.indexOf("'", startIndex);
            return errorMessage.substring(startIndex, endIndex);
        } catch (Exception e) {
            return "unknown";
        }
    }

}
