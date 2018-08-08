package com.davewiard;

import java.util.regex.Pattern;

final class ZipCodeValidator {
    private final static String patternString = "^[0-9]{5}(?:-[0-9]{4})?$";
    private final static Pattern pattern = Pattern.compile(patternString);

    /**
     * Validates if the input string contains a value formatted as a U.S. ZIP
     * code. The ZIP code may be in ##### or #####-#### format.
     * @param zipCode The ZIP code value to be evaluated
     * @return boolean representing if the input value is in a valid U.S. ZIP code format
     */
    static boolean isValidZipCode(String zipCode) {
        return pattern.matcher(zipCode).matches();
    }
}
