package com.zsgs.busbooking.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrediantialsValidator {

    private static final Pattern EMAIL_REGEX_PATTERN =
           Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    private static final Pattern MOBILE_REGEX_PATTERN = Pattern.compile("^[7-9][0-9]{9}$");

    public boolean validateMobileNumber(String mobile) {

        if ( mobile == null) return false;

        Matcher matcher = MOBILE_REGEX_PATTERN.matcher(mobile);

        return matcher.matches();
    }

     public boolean validateEmail(String email) {

        if ( email == null) return false ;
        Matcher matcher = EMAIL_REGEX_PATTERN.matcher(email);
        return matcher.matches();
     }


}
