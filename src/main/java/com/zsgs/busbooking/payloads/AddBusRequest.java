package com.zsgs.busbooking.payloads;

import com.zsgs.busbooking.enums.BusStatus;


public class AddBusRequest {

    private String busName;
    private String busRegistrationId ;
    private String busType;
    private String busNumber;
    private BusStatus busStatus;

    public AddBusRequest() {
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

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public BusStatus getBusStatus() {
        return busStatus;
    }

    public void setBusStatus(BusStatus busStatus) {
        this.busStatus = busStatus;
    }
}
