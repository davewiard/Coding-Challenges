package com.davewiard;

public class Main {

    public static String apiKeyGoogle = System.getenv("API_KEY_GOOGLE");
    public static String apiKeyOWM = System.getenv("API_KEY_OWM");

    public static void main(String[] args) {

        CityData cityData = new CityData();

        // get and save required API keys
        if (getApiKeys() == false) {
            return;
        }

        // check for ZIP code passed in on the command-line
        if (args.length > 0) {
            cityData.setZipCode(args[0]);
        } else {
            // not provided on command-line, request from user

        }

        // fail if the given zip code is in an invalid format
        boolean isZipCodeValid = ZipCodeValidator.isValidZipCode(cityData.getZipCode());
        if (isZipCodeValid == false) {
            System.err.println("ZIP code provided (" + cityData.getZipCode() + ") is invalid.");
            return;
        } else {
            System.out.println("ZIP code provided (" + cityData.getZipCode() + ") is valid.");
        }
    }


    /**
     * Get and save both the Google and OpenWeatherMap API keys from the environment.
     * @return boolean representing if both required API keys were found
     */
    public static boolean getApiKeys() {
        // validate whether the API keys are defined in the environment
        if (apiKeyGoogle.length() == 0) {
            System.err.println("API_KEY_GOOGLE environment variable not defined or empty");
            return false;
        }

        if (apiKeyOWM.length() == 0) {
            System.err.println("API_KEY_OWM environment variable not defined or empty");
            return false;
        }

        return true;
    }
}
