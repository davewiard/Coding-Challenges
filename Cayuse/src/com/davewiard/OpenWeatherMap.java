package com.davewiard;

import com.github.prominence.openweathermap.api.OpenWeatherMapManager;
import com.github.prominence.openweathermap.api.WeatherRequester;
import com.github.prominence.openweathermap.api.constants.Accuracy;
import com.github.prominence.openweathermap.api.constants.Language;
import com.github.prominence.openweathermap.api.constants.Unit;
import com.github.prominence.openweathermap.api.exception.DataNotFoundException;
import com.github.prominence.openweathermap.api.model.response.Weather;

final class OpenWeatherMap {

    private static Float latitude;
    private static Float longitude;

    private static String cityName;
    private static Float temperature;

    private OpenWeatherMapManager openWeatherMapManager;


    /**
     * Object constructor
     * @param apiKeyOWM OpenWeatherMap API key
     */
    OpenWeatherMap(String apiKeyOWM) {
        openWeatherMapManager = new OpenWeatherMapManager(apiKeyOWM);
    }


    /**
     * Get the city name, temperature, latitude, and longitude from OpenWeatherMap API
     * @param zipCode ZIP code representing the city to look up
     */
    boolean getOpenWeatherMapData(String zipCode) {
        // get the city name and temp for the given ZIP code
        try {
            WeatherRequester weatherRequester = openWeatherMapManager.getWeatherRequester();
            Weather weatherResponse = weatherRequester
                    .setLanguage(Language.ENGLISH)
                    .setUnitSystem(Unit.IMPERIAL_SYSTEM)
                    .setAccuracy(Accuracy.ACCURATE)
                    .getByZIPCode(zipCode, "US");

            cityName = weatherResponse.getCityName();
            latitude = weatherResponse.getCoordinates().getLatitude();
            longitude = weatherResponse.getCoordinates().getLongitude();
            temperature = weatherResponse.getTemperature();
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
        return cityName;
    }

    Float getLatitude() {
        return latitude;
    }

    Float getLongitude() {
        return longitude;
    }

    Float getTemperature() {
        return temperature;
    }
}
