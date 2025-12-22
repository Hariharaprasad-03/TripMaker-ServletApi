package com.zsgs.busbooking.payloads;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record UserBookingDto(

        // Booking
        String bookingId,
        String passengerId,
        String bookingStatus,
        int noOfSeats,
        double price,
        LocalDateTime createdAt,
        List<Integer> seatNumbers,

        // Trip
        String tripId,
        String source,
        String destination,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        double distanceKm,

        // Bus
        String busId,
        String busName,
        String busNumber
) {}
