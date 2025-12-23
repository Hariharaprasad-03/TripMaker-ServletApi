package com.zsgs.busbooking.payloads;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public record UpdatetripRequest(

    String tripId ,
    LocalDate date ,
    LocalTime startTime ,
    LocalTime endTime
    ) implements Serializable {
}
