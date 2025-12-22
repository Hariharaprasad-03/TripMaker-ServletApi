package com.zsgs.busbooking.model;
import com.zsgs.busbooking.enums.TripStatus;

import java.time.LocalDate;
import java.time.LocalTime;


public class Trip {

    private String tripId;
    private String busId;
    private String routeId;
    private LocalTime startTime;
    private LocalTime endTime;
    private TripStatus staus ;
    private LocalDate tripDate;
    private double price ;

    public Trip() {}

    public Trip(String tripId, String busId, String routeId,
                LocalTime startTime, LocalTime endTime) {
        this.tripId = tripId;
        this.busId = busId;
        this.routeId = routeId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public TripStatus getStaus() {
        return staus;
    }

    public void setStaus(TripStatus staus) {
        this.staus = staus;
    }

    public LocalDate getTripDate() {
        return tripDate;
    }

    public void setTripDate(LocalDate tripDate) {
        this.tripDate = tripDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
