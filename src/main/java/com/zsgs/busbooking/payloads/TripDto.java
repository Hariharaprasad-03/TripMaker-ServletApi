package com.zsgs.busbooking.payloads;

import com.zsgs.busbooking.enums.BusType;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public record TripDto(
        String tripId ,
        String busName ,
        String busNumber ,
        String source ,
        String destination ,
        int distanceKm,
        LocalTime startTime ,
        LocalTime endTime ,
        LocalDate date ,
        BusType busType,
        double price
) implements Serializable {

    @Override
    public String toString() {
        return "TripDto{" +
                "tripId='" + tripId + '\'' +
                ", busName='" + busName + '\'' +
                ", busNumber='" + busNumber + '\'' +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", distanceKm=" + distanceKm +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", date=" + date +
                ", busType=" + busType +
                ", price=" + price +
                '}';
    }
}
