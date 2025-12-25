package com.zsgs.busbooking.payloads;

import com.zsgs.busbooking.enums.SeatStatus;
import com.zsgs.busbooking.enums.SeatType;

import java.io.Serializable;

public record SeatDto(

        int searNumber,
        int rowNumber ,
        int colNumber ,
        SeatStatus status,
        SeatType seatType
) implements Serializable {


}
