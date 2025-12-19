package com.zsgs.busbooking.model;

import com.zsgs.busbooking.enums.BusStatus;
import com.zsgs.busbooking.enums.BusType;

import java.time.LocalDate;

public class Bus {

    private String busId;
    private String busName;
    private String busRegistrationId ;
    private BusType busType;
    private String busNumber;
    private BusStatus busStatus;

    public Bus() {}

    public Bus(String busId, String busName, String busModel, String busNumber,BusType busType, LocalDate busRegistrationDate , String busRegistrationId) {
        this.busId = busId;
        this.busName = busName;
        this.busType = busType;
        this.busNumber = busNumber ;
        this.busRegistrationId = busRegistrationId ;

    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getBusRegistrationId() {
        return busRegistrationId;
    }

    public void setBusRegistrationId(String busRegistrationId) {
        this.busRegistrationId = busRegistrationId;
    }

    public BusType getBusType() {
        return busType;
    }

    public void setBusType(BusType busType) {
        this.busType = busType;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public BusStatus getBusStatus() {
        return busStatus;
    }

    public void setBusStatus(BusStatus busStatus) {
        this.busStatus = busStatus;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }
}
