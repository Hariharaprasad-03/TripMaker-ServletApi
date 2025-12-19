package com.zsgs.busbooking.util;

public class IdGenerator {

    public String genarateId(String prefix , long id){

        String generatedId =prefix + String.format("%03d", id);

        return generatedId;

    }
}
