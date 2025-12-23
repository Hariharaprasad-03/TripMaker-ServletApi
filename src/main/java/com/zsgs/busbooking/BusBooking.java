package com.zsgs.busbooking;

import com.zsgs.busbooking.config.AppContext;
import com.zsgs.busbooking.config.BeanFactory;
import com.zsgs.busbooking.enums.BusType;
import com.zsgs.busbooking.model.Bus;
import com.zsgs.busbooking.model.Route;
import com.zsgs.busbooking.payloads.*;
import com.zsgs.busbooking.repositories.BusRepository;
import com.zsgs.busbooking.repositories.PassengerRepository;

import com.zsgs.busbooking.services.*;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

            AddBusRequest busRequest2 = new AddBusRequest();

            busRequest2.setBusNumber("TN23BR4844");
            busRequest2.setBusName("SKMR");
            busRequest2.setBusRegistrationId("12345679");
            busRequest2.setBusType(String.valueOf(BusType.NORMAL));

            busService.addBus(busRequest);
            busService.addBus(busRequest2);

            PassengerSignUpRequest p1 = new PassengerSignUpRequest();
            p1.setEmail("san@gmail.com");
            p1.setMobileNumber("9278111111");
            p1.setPassword("abcd");
            p1.setPassengerName("SANJAY");

            passengerService.addPassenger(p1);

            RouteService routeService = AppContext.getInstance().getRouteService();
            Route route = BeanFactory.getInstance().createRoute();
            route.setSource("vellore");
            route.setDestination("Arni");
            route.setDistanceKm(40);

            Route velloreToThiruppathur = BeanFactory.
                    getInstance().createRoute();

            velloreToThiruppathur.setSource("vellore");
            velloreToThiruppathur.setDestination("thiruppathur");
            velloreToThiruppathur.setDistanceKm(85);

            routeService.addRouteService(velloreToThiruppathur);
            routeService.addRouteService(route);

            CreateTripRequest tripRequest1 = new CreateTripRequest("TN23BR4843", "SKMR", "vellore", "arni", LocalTime.now(), LocalTime.of(18,00), LocalDate.now());
            CreateTripRequest tripRequest2 = new CreateTripRequest("TN23BR4844", "SKMS", "vellore", "thiruppathur", LocalTime.now(), LocalTime.of(19,00), LocalDate.now());

            TripService tripService = AppContext.getInstance().getTripService();

            tripService.createTrip(tripRequest1);
            tripService.createTrip(tripRequest2);

            List<TripDto> dto = tripService.getCurrentTrips();

            for ( TripDto d : dto){
                System.out.println(d);
            }
            List<TripDto> getdtos = tripService.getAllTripsBySourceAndDestinationWithDate("vellore" , "arni",LocalDate.now());

            for(TripDto d: getdtos){
                System.out.println(d);
            }

            List<Integer> seats = new ArrayList<>();
            seats.addAll(List.of(1,2,3,4));

            BookingRequest bookingRequest = new BookingRequest("TRIP001",
                    "BUS001" ,
                    seats,
                    "9278111111",
                    "san@oksbi ",
                    seats.size()
            );

            BookingServices bookingServices = AppContext.getInstance().getBookingServices();

            bookingServices.BookTripWithAtomicity(bookingRequest);


            List<BusDto> busdtos = busService.getAllBuses();

            for(BusDto busdto : busdtos){

                System.out.println(busdto);
            }





        } catch (SQLIntegrityConstraintViolationException e) {

            e.printStackTrace();
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

            e.printStackTrace();
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
