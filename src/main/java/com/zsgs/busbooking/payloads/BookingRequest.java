package com.zsgs.busbooking.payloads;

import java.io.Serializable;
import java.util.List;

public record BookingRequest(
        String tripId,
        String busId,
        List<Integer> seats,

        String regusteredMobileNumber,
        String paymentId,
        int numberOfSeats
)implements Serializable {
}
