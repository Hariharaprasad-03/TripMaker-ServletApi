package com.zsgs.busbooking.payloads;

import java.io.Serializable;

public record PassengerDto(
        String passengerName ,
        String passengerId ,
        String email ,
        String mobileNumber
)implements Serializable {
}
