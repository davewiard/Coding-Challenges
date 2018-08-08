package com.davewiard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TimeZone;

import com.github.prominence.openweathermap.api.OpenWeatherMapManager;
import com.github.prominence.openweathermap.api.WeatherRequester;
import com.github.prominence.openweathermap.api.constants.Accuracy;
import com.github.prominence.openweathermap.api.constants.Language;
import com.github.prominence.openweathermap.api.constants.Unit;
import com.github.prominence.openweathermap.api.model.response.Weather;

import com.google.maps.ElevationApi;
import com.google.maps.GeoApiContext;
import com.google.maps.TimeZoneApi;
import com.google.maps.model.ElevationResult;
import com.google.maps.model.LatLng;

public class Main {

    private static String apiKeyGoogle = System.getenv("API_KEY_GOOGLE");
    private static String apiKeyOWM = System.getenv("API_KEY_OWM");
    private static GeoApiContext context = new GeoApiContext();

    public static void main(String[] args) {
        // get and save required API keys
        if (!getApiKeys()) {
            return;
        }
        context.setApiKey(apiKeyGoogle);

        // get all ZIP codes from the command-line (or user STDIN if none give on command-line)
        // loop through each ZIP code to retrieve OpenWeatherMap and Google data for the given ZIP code
        ArrayList<CityData> cityDataArrayList = getZipCode(args);
        for (CityData cityData : cityDataArrayList) {
            System.out.println("ZIP code: " + cityData.getZipCode());

            // retrieve the OpenWeatherMap data for the given ZIP code
            getOpenWeatherMapData(cityData);

            // use one LatLng object for both Google API calls
            LatLng latLng = new LatLng(cityData.getLatitude(), cityData.getLongitude());

            // get the elevation for the coordinates returned by OpenWeatherMap
            Double elevation = GoogleMapsApi.getElevation(context, latLng);
            cityData.setElevation(elevation);

            // get the timezone for the coordinates returned by OpenWeatherMap
            String timeZone = GoogleMapsApi.getTimeZone(context, latLng);
            cityData.setTimeZone(timeZone);

            // sleep for 500 ms to help with quota limit issues
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }

            System.out.println("At the location " + cityData.getCityName() +
                               ", the temperature is " + cityData.getTemperature() +
                               ", the timezone is " + cityData.getTimeZone() +
                               ", and the elevation is " + cityData.getElevation());
        }
    }


    /**
     *
     * @param cityData
     */
    private static void getOpenWeatherMapData(CityData cityData) {
        // get the city name and temp for the given ZIP code
        try {
            OpenWeatherMapManager openWeatherMapManager = new OpenWeatherMapManager(apiKeyOWM);
            WeatherRequester weatherRequester = openWeatherMapManager.getWeatherRequester();
            Weather weatherResponse = weatherRequester
                    .setLanguage(Language.ENGLISH)
                    .setUnitSystem(Unit.IMPERIAL_SYSTEM)
                    .setAccuracy(Accuracy.ACCURATE)
                    .getByZIPCode(cityData.getZipCode(), "US");

//            System.out.println(weatherResponse.toString());

            cityData.setTemperature(weatherResponse.getTemperature());
            cityData.setCityName(weatherResponse.getCityName());
            cityData.setLatitude(weatherResponse.getCoordinates().getLatitude());
            cityData.setLongitude(weatherResponse.getCoordinates().getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Get and save both the Google and OpenWeatherMap API keys from the environment.
     * @return boolean representing if both required API keys were found
     */
    private static boolean getApiKeys() {
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

        // TODO
        // Can the following loop be rewritten as a lambda expression?

        // Verify if the given ZIP codes are in a valid format
        // Only keep valid input but print errors about invalid formatted entries
        for (String zipCode : zipCodeArrayList) {
            // verify if the given zip code is in an invalid format
            if (!ZipCodeValidator.isValidZipCode(zipCode)) {
                System.err.println(zipCode + " is not a valid U.S. ZIP code.");
            } else {
                CityData cityData = new CityData(zipCode);
                cityDataArrayList.add(cityData);
            }
        }

        return cityDataArrayList;
    }
}
