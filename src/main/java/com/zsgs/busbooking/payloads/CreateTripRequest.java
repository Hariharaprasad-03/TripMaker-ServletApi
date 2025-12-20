package com.zsgs.busbooking.payloads;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public record CreateTripRequest (

        String busId,
        String busNumber,
        String busName ,
        String source,
        String destination,
        LocalTime startTime,
        LocalTime endTime ,
        LocalDate tripDate
) implements Serializable {

}
