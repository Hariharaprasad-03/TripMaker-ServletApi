package com.zsgs.busbooking.payloads;

import com.zsgs.busbooking.enums.TripStatus;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public record CreateTripRequest (

        String busId,
        String routeId,
        LocalTime startTime,
        LocalTime endTime ,
        LocalDate tripDate
) implements Serializable {

}
