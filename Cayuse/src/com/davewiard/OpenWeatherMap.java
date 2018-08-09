package com.davewiard;

import com.github.prominence.openweathermap.api.OpenWeatherMapManager;
import com.github.prominence.openweathermap.api.WeatherRequester;
import com.github.prominence.openweathermap.api.constants.Accuracy;
import com.github.prominence.openweathermap.api.constants.Language;
import com.github.prominence.openweathermap.api.constants.Unit;
import com.github.prominence.openweathermap.api.exception.DataNotFoundException;
import com.github.prominence.openweathermap.api.model.response.Weather;

/**
 * Get specific data from the OpenWeatherMap API for the requested U.S. ZIP code. Need the city name, temperature (in
 * Fahrenheit), latitude, and longitude. The latitude and longitude are used elsewhere to obtain data from the Google
 * Maps API.
 */
final class OpenWeatherMap {

    private static WeatherRequester weatherRequester;
    private Weather weatherResponse;


    /**
     * Class constructor requires the OpenWeatherMap API key so multiple requests to OpenWeatherMap could be done
     * sing the same WeatherRequester even though we do not need to do this for this code challenge.
     * @param apiKeyOWM OpenWeatherMap API key
     */
    OpenWeatherMap(String apiKeyOWM) {
        OpenWeatherMapManager openWeatherMapManager = new OpenWeatherMapManager(apiKeyOWM);
        weatherRequester = openWeatherMapManager.getWeatherRequester();
    }


    /**
     * Get the city name, temperature, latitude, and longitude from OpenWeatherMap API
     * @param zipCode ZIP code representing the city to look up
     */
    boolean getOpenWeatherMapData(String zipCode) {
        // get the city name and temp for the given ZIP code
        try {
            weatherResponse = weatherRequester
                    .setLanguage(Language.ENGLISH)
                    .setUnitSystem(Unit.IMPERIAL_SYSTEM)
                    .setAccuracy(Accuracy.ACCURATE)
                    .getByZIPCode(zipCode, "US");
        } catch (DataNotFoundException dnfe) {
            System.err.println("No data found for ZIP code (" + zipCode + ")");
            return false;
        } catch (Exception e) {
            System.err.println("Failed to get data for ZIP code (" + zipCode + ")");
            e.printStackTrace();
            return false;
        }

        return true;
    }


    String getCityName() {
        return (weatherResponse == null) ? null : weatherResponse.getCityName();
    }


    Float getLatitude() {
        return (weatherResponse == null) ? null : weatherResponse.getCoordinates().getLatitude();
    }


    Float getLongitude() {
        return (weatherResponse == null) ? null : weatherResponse.getCoordinates().getLongitude();
    }


    Float getTemperature() {
        return (weatherResponse == null) ? null : weatherResponse.getTemperature();
    }
}
