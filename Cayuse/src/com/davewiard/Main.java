package com.davewiard;

import java.util.ArrayList;
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

        // check for ZIP code passed in on the command-line
        ArrayList<CityData> cityDataArrayList = getZipCode(args);
        for (CityData cityData : cityDataArrayList) {
            System.out.println("ZIP code: " + cityData.getZipCode());

            getOpenWeatherMapData(cityData);

            // use one LatLng object for both Google API calls
            LatLng latLng = new LatLng(cityData.getLatitude(), cityData.getLongitude());

            getElevationFromGoogle(cityData, latLng);
            getTimeZoneFromGoogle(cityData, latLng);

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
     * @param latLng
     */
    private static void getTimeZoneFromGoogle(CityData cityData, LatLng latLng) {
        try {
            TimeZone timeZone = TimeZoneApi.getTimeZone(context, latLng).await();
            cityData.setTimeZone(timeZone.getDisplayName());
//            System.out.println("time zone = " + cityData.getTimeZone());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @param cityData
     * @param latLng
     */
    private static void getElevationFromGoogle(CityData cityData, LatLng latLng) {
        // get the elevation for the given coordinates
        try {
            ElevationResult result = ElevationApi.getByPoint(context, latLng).await();
            cityData.setElevation(result.elevation);
//            System.out.println("elevation = " + cityData.getElevation());
        } catch (Exception e) {
            e.printStackTrace();
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
     *
     * @param args
     * @return
     */
    private static ArrayList<CityData> getZipCode(String[] args) {
        ArrayList<String> zipCodeArrayList = new ArrayList<>();
        ArrayList<CityData> cityDataArrayList = new ArrayList<>();

        if (args.length > 0) {
            // add all command-line arguments to the list
            for (String arg : args) {
                zipCodeArrayList.add(arg);
            }
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
