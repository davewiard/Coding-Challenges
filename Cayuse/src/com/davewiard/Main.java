package com.davewiard;

import com.google.maps.GeoApiContext;
import com.google.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private static String apiKeyGoogle = System.getenv("API_KEY_GOOGLE");
    private static String apiKeyOWM = System.getenv("API_KEY_OWM");
    private static GeoApiContext geoApiContext = new GeoApiContext();

    public static void main(String[] args) {
        // check that the required API keys have a value assigned
        if (!checkApiKeys()) {
            return;
        }

        // initialize the Google API context with the API key
        geoApiContext.setApiKey(apiKeyGoogle);

        // get all ZIP codes from the command-line (or user STDIN if none give on command-line)
        // loop through each ZIP code to retrieve OpenWeatherMap and Google data for the given ZIP code
        ArrayList<CityData> cityDataArrayList = getZipCode(args);
        for (CityData cityData : cityDataArrayList) {

            // retrieve the OpenWeatherMap data for the given ZIP code
            getOpenWeatherMapData(cityData);
            if (cityData.getLatitude() == null || cityData.getLongitude() == null) {
                // failed to get requested data, move on to next ZIP code
                continue;
            }

            // use one LatLng object for both Google API calls
            LatLng latLng = new LatLng(cityData.getLatitude(), cityData.getLongitude());

            // get the elevation for the coordinates returned by OpenWeatherMap
            Double elevation = GoogleMapsApi.getElevation(geoApiContext, latLng);
            if (elevation == null) {
                System.err.println("Failed to get elevation for " + cityData.getZipCode());
                continue;
            } else {
                cityData.setElevation(elevation);
            }

            // get the timezone for the coordinates returned by OpenWeatherMap
            String timeZone = GoogleMapsApi.getTimeZone(geoApiContext, latLng);
            if (timeZone == null) {
                System.err.println("Failed to get timezone for " + cityData.getZipCode());
                continue;
            } else {
                cityData.setTimeZone(timeZone);
            }

            // sleep for 500 ms to help with quota limit issues
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
                continue;
            }

            System.out.println("At the location " + cityData.getCityName() +
                               ", the temperature is " + cityData.getTemperature() +
                               ", the timezone is " + cityData.getTimeZone() +
                               ", and the elevation is " + cityData.getElevation());
        }
    }


    /**
     * Wrapper for getting the data returned by OpenWeatherMap API into our CityData object.
     * @param cityData destination object for the OpenWeatherMap API data
     */
    private static void getOpenWeatherMapData(CityData cityData) {
        OpenWeatherMap openWeatherMap = new OpenWeatherMap(apiKeyOWM);
        openWeatherMap.getOpenWeatherMapData(cityData.getZipCode());

        cityData.setCityName(openWeatherMap.getCityName());
        cityData.setLatitude(openWeatherMap.getLatitude());
        cityData.setLongitude(openWeatherMap.getLongitude());
        cityData.setTemperature(openWeatherMap.getTemperature());
    }


    /**
     * Get both the Google and OpenWeatherMap API keys from the environment.
     * @return boolean representing if both required API keys were found
     */
    private static boolean checkApiKeys() {
        //
        // TODO
        // Getting the API keys from the environment is not ideal. A better solution is
        // to store them in a file with restricted permissions.
        //

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


    /**
     * Get a list of ZIP code strings from either the command-line. If none given on command-line prompt the
     * user to enter one ZIP code per line on STDIN.
     * @param args Arguments passed in from the command-line
     * @return ArrayList of ZIP code string values
     */
    private static ArrayList<CityData> getZipCode(String[] args) {
        ArrayList<String> zipCodeArrayList = new ArrayList<>();
        ArrayList<CityData> cityDataArrayList = new ArrayList<>();

        if (args.length > 0) {
            zipCodeArrayList.addAll(Arrays.asList(args));
        } else {
            // no parameters passed in on command-line, read from STDIN
            try (Scanner scanner = new Scanner(System.in)) {
                String input;
                System.out.println("Please enter ZIP codes one per line (blank line to stop entering):");
                do {
                    input = scanner.nextLine();
                    if (input.length() > 0) {
                        zipCodeArrayList.add(input);
                    }
                } while (input.length() != 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Verify if the given ZIP codes are in a valid format. Only keep valid input
        // but print errors about invalid formatted entries. Also note that being a
        // valid format does not mean the ZIP code is a valid ZIP code (e.g. "00000"
        // is a valid format but is not a valid ZIP code).
        for (String zipCode : zipCodeArrayList) {
            // verify if the given zip code is in an invalid format
            if (!ZipCodeValidator.isValidZipCode(zipCode)) {
                System.err.println(zipCode + " is not a valid U.S. ZIP code.");
            } else {
                cityDataArrayList.add(new CityData(zipCode));
            }
        }

        return cityDataArrayList;
    }
}
