package com.zsgs.busbooking.payloads;

import com.zsgs.busbooking.enums.BusStatus;
import com.zsgs.busbooking.enums.BusType;

import java.io.Serializable;

public class BusDto implements Serializable {

    private String busId;
    private String busName;
    private String busRegistrationId ;
    private BusType busType;
    private String busNumber;
    private BusStatus busStatus;
    private TripDto tripDto ;

    public BusDto() {
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

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public BusStatus getBusStatus() {
        return busStatus;
    }

    public void setBusStatus(BusStatus busStatus) {
        this.busStatus = busStatus;
    }

    public TripDto getTripDto() {
        return tripDto;
    }

    public void setTripDto(TripDto tripDto) {
        this.tripDto = tripDto;
    }

    @Override
    public String toString() {
        return "BusDto{" +
                "busId='" + busId + '\'' +
                ", busName='" + busName + '\'' +
                ", busRegistrationId='" + busRegistrationId + '\'' +
                ", busType=" + busType +
                ", busNumber='" + busNumber + '\'' +
                ", busStatus=" + busStatus +
                ", tripDto=" + tripDto +
                '}';
    }
}
