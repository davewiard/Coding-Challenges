package com.davewiard;

import java.util.regex.Pattern;

public class ZipCodeValidator {
    final static String patternString = "^[0-9]{5}(?:-[0-9]{4})?$";
    private static Pattern pattern = Pattern.compile(patternString);;

    public boolean validate(String zipCode) {
        return pattern.matcher(zipCode).matches();
    }
}
