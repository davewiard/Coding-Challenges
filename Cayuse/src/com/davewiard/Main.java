package com.davewiard;

import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static String apiKeyGoogle = System.getenv("API_KEY_GOOGLE");
    private static String apiKeyOWM = System.getenv("API_KEY_OWM");

    public static void main(String[] args) {

        // get and save required API keys
        if (!getApiKeys()) {
            return;
        }

        // check for ZIP code passed in on the command-line
        ArrayList<CityData> cityDataArrayList = getZipCode(args);
        for (CityData cityData : cityDataArrayList) {
            System.out.println("ZIP code: " + cityData.getZipCode());

            OWM owm = new OWM(apiKeyOWM);
            owm.setUnit(OWM.Unit.IMPERIAL);     // use Fahrenheit units instead of Kelvin
            try {
                CurrentWeather currentWeather = owm.currentWeatherByZipCode(Integer.parseInt(cityData.getZipCode()));
                // checking data retrieval was successful or not
                if (currentWeather.hasRespCode() && currentWeather.getRespCode() == 200) {
                    // get and save the city name if that field is available
                    if (currentWeather.hasCityName()) {
                        cityData.setCityName(currentWeather.getCityName());
                        System.out.println("city name: " + cityData.getCityName());
                    } else {
                        // TODO
                        // INCOMPLETE DATA RETURNED
                    }

                    // get and save the current temperature if that field is available
                    if (currentWeather.hasMainData() && currentWeather.getMainData().hasTemp()) {
                        cityData.setTemperature(currentWeather.getMainData().getTemp());
                        System.out.println("city temp: " + cityData.getTemperature());
                    } else {
                        // TODO
                        // INCOMPLETE DATA RETURNED
                    }

//                    currentWeather.getCoordData()
                } else {
                    // TODO
                    // ERROR CONDITION, DATA CANNOT BE TRUSTED
                }
            } catch (APIException apie) {
                apie.printStackTrace();
            }
        }
    }


    /**
     * Get and save both the Google and OpenWeatherMap API keys from the environment.
     * @return boolean representing if both required API keys were found
     */
    public static boolean getApiKeys() {

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
