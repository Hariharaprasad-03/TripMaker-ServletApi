package com.zsgs.busbooking;

import com.zsgs.busbooking.enums.BusType;
import com.zsgs.busbooking.payloads.AddBusRequest;
import com.zsgs.busbooking.payloads.CreateTripRequest;
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


            AddBusRequest busRequest = new AddBusRequest();

            busRequest.setBusNumber("TN23BR4843");
            busRequest.setBusName("SKMS");
            busRequest.setBusRegistrationId("12345678");
            busRequest.setBusType(String.valueOf(BusType.NORMAL));

            busService.addBus(busRequest);

            PassengerSignUpRequest p1 = new PassengerSignUpRequest();
            p1.setEmail("san@gmail.com");
            p1.setMobileNumber("9278111111");
            p1.setPassword("abcd");
            p1.setPassengerName("SANJAY");

            passengerService.addPassenger(p1);











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
