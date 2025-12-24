package com.zsgs.busbooking.services;

import com.zsgs.busbooking.config.DatabaseConfig;
import com.zsgs.busbooking.enums.SeatStatus;
import com.zsgs.busbooking.enums.TripStatus;
import com.zsgs.busbooking.exception.InvalidRequest;
import com.zsgs.busbooking.model.*;
import com.zsgs.busbooking.payloads.BookingRequest;

import com.zsgs.busbooking.payloads.UserBookingDto;
import com.zsgs.busbooking.repositories.BookingRepository;
import com.zsgs.busbooking.repositories.PaymentRepository;
import com.zsgs.busbooking.util.IdGenerator;
import com.zsgs.busbooking.util.IdGeneratorUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BookingServices {

    private BookingRepository bookingRepository = new BookingRepository() ;
    private PassengerService passengerService ;
    private BusService busService ;
    private TripService tripService ;
    private static  long id  = 1;

    public BookingServices(PassengerService passengerService, BusService busService, TripService tripService){
        this.busService = busService;
        this.passengerService = passengerService;
        this.tripService = tripService;
    }

    public synchronized  boolean BookTrip(BookingRequest request)throws SQLException {

        try {
            if (request.busId().isEmpty() || request.tripId().isEmpty() || request.seats().isEmpty() || request.regusteredMobileNumber().isEmpty()) {
                throw new InvalidRequest(" Invalid request  insufficent data to Book the Ticket ");
            }

            String busId = request.busId().trim();
            String tripId = request.tripId().trim();
            List<Integer> seats = request.seats();
//            String passengerId = request.passengerId().trim();
            String mobileNumber = request.regusteredMobileNumber().trim();
            String paymentId = request.paymentId().trim();

            Bus bus = busService.getBusById(busId);
            Trip trip = tripService.getTripById(tripId);

            if (bus == null || trip == null) {

                throw new InvalidRequest("invalid request ");
            }

            if (trip.getStaus() != TripStatus.ACTIVE) {
                throw new InvalidRequest(" trip is Not Active now ");
            }

            Passenger passenger = passengerService.getPassengerByMobileNumber(mobileNumber);

            if (passenger == null) {

                throw new InvalidRequest(" no passenger Exist please Log in");
            }

            if( ! checkSeatStatus(seats,tripId)){

                throw  new InvalidRequest(" seat is Not Available Now ");
            }


            Payment payment = new Payment();
            payment.setPaymentAddress(paymentId);
            payment.setPassengerId(passenger.getPassengerId());
            payment.setPaymentType("upi");
            payment.setAmount(seats.size() * trip.getPrice());
            payment.setPaymentStatus("success");

            PaymentServices paymentServices = new PaymentServices( new PaymentRepository());
            paymentServices.pay(payment);


            Booking booking = Booking.builder().
                    withBookingId(new IdGenerator().genarateId("BOOK", id++)).
                    withBusId(busId).
                    withPaymentId(payment.getPaymentId()).
                    withPrice(seats.size() * trip.getPrice()).
                    withTripId(tripId).
                    withNoOfSeats(request.numberOfSeats()).
                    withUserId(passenger.getPassengerId()).
                    build();



            if (bookingRepository.bookTrip(booking)) {

                    if( !bookingRepository.BookSeats(busId,booking.getBookingId(),tripId,seats)){

                        System.out.println("Booking failed Revoking start");
                        bookingRepository.revokeBooking(booking);
                        bookingRepository.revokeSeats(busId,tripId,seats);
;                    }
            }
        }catch (SQLException e) {

            throw  e;
        }

        return true ;

    }

    public  boolean  checkSeatStatus (List<Integer> seats , String tripId)throws SQLException{

        for ( int seatNumber : seats){

            if (bookingRepository.chcekSeatStatus(tripId,seatNumber) != SeatStatus.AVAILABLE){

                return false;
            }
        }
        return true;
    }


    public boolean BookTripWithAtomicity(BookingRequest request) throws SQLException {
        // 1. Get a single connection for the entire operation
        Connection conn = DatabaseConfig.getInstance().getConnection();

        try {

            conn.setAutoCommit(false);

            if (request.busId().isEmpty() || request.tripId().isEmpty() || request.seats().isEmpty() || request.regusteredMobileNumber().isEmpty()) {
                throw new InvalidRequest(" Invalid request  insufficent data to Book the Ticket ");
            }

            String busId = request.busId().trim();
            String tripId = request.tripId().trim();
            List<Integer> seats = request.seats();
//            String passengerId = request.passengerId().trim();
            String mobileNumber = request.regusteredMobileNumber().trim();
            String paymentId = request.paymentId().trim();

            Bus bus = busService.getBusById(busId);
            Trip trip = tripService.getTripById(tripId);

            if (bus == null || trip == null) {

                throw new InvalidRequest("invalid request ");
            }

            if (trip.getStaus() != TripStatus.ACTIVE) {
                throw new InvalidRequest(" trip is Not Active now ");
            }

            Passenger passenger = passengerService.getPassengerByMobileNumber(mobileNumber);

            if (passenger == null) {

                throw new InvalidRequest(" no passenger Exist please Log in");
            }

            if( ! checkSeatStatus(seats,tripId)){

                throw  new InvalidRequest(" seat is Not Available Now ");
            }

            // ... validation logic (Bus, Trip, Passenger checks) ...

            // 3. Save Payment (Passing the connection)

            Payment payment = new Payment();
            payment.setPaymentAddress(paymentId);
            payment.setPassengerId(passenger.getPassengerId());
            payment.setPaymentType("upi");
            payment.setAmount(seats.size() * trip.getPrice());
            payment.setPaymentStatus("success");

            PaymentServices paymentServices = new PaymentServices( new PaymentRepository());
            paymentServices.pay( conn ,payment);
//            paymentRepository.pay(conn, payment);

            Booking booking = Booking.builder().
                    withBookingId(new IdGeneratorUtil().generateId("BOOKING")).
                    withBusId(busId).
                    withPaymentId(payment.getPaymentId()).
                    withPrice(seats.size() * trip.getPrice()).
                    withTripId(tripId).
                    withNoOfSeats(request.numberOfSeats()).
                    withUserId(passenger.getPassengerId()).
                    build();

            // 4. Save Booking (Passing the connection)
            bookingRepository.bookTrip(conn, booking);

            // 5. Book Seats (Passing the connection)
            bookingRepository.BookSeats(conn, busId,booking.getBookingId(), tripId, seats);

            // 6. If we reached here without an exception, push all changes to DB
            conn.commit();
            System.out.println("Transaction Committed Successfully!");
            return true;

        } catch (Exception e) {
            // 7. If ANYTHING fails, undo everything since setAutoCommit(false)
            if (conn != null) {
                System.err.println("Error detected. Rolling back all changes...");
                conn.rollback();
            }
            throw e; // Re-throw to inform the caller
        } finally {
            // 8. Always close the connection and reset auto-commit
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public List<UserBookingDto> getPassengerBookings(String passengerId) throws SQLException{

        return bookingRepository.getPassengerBookings(passengerId);
    }


}
