package com.davewiard;

import com.github.prominence.openweathermap.api.OpenWeatherMapManager;
import com.github.prominence.openweathermap.api.WeatherRequester;
import com.github.prominence.openweathermap.api.constants.Accuracy;
import com.github.prominence.openweathermap.api.constants.Language;
import com.github.prominence.openweathermap.api.constants.Unit;
import com.github.prominence.openweathermap.api.model.response.Weather;

final class OpenWeatherMapApi {

    private static Float latitude;
    private static Float longitude;

    private static String cityName;
    private static Float temperature;

    /**
     * Prevent instantiation of this class. There is no need for it. Access the
     * methods directly through a class reference.
     */
    private OpenWeatherMapApi() {}


    /**
     *
     * @param cityData
     */
    public static void getOpenWeatherMapData(String apiKeyOWM, String zipCode) {
        // get the city name and temp for the given ZIP code
        try {
            OpenWeatherMapManager openWeatherMapManager = new OpenWeatherMapManager(apiKeyOWM);
            WeatherRequester weatherRequester = openWeatherMapManager.getWeatherRequester();
            Weather weatherResponse = weatherRequester
                    .setLanguage(Language.ENGLISH)
                    .setUnitSystem(Unit.IMPERIAL_SYSTEM)
                    .setAccuracy(Accuracy.ACCURATE)
                    .getByZIPCode(zipCode, "US");

//            System.out.println(weatherResponse.toString());

            cityName = weatherResponse.getCityName();
            latitude = weatherResponse.getCoordinates().getLatitude();
            longitude = weatherResponse.getCoordinates().getLongitude();
            temperature = weatherResponse.getTemperature();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCityName() {
        return cityName;
    }

    public static Float getLatitude() {
        return latitude;
    }

    public static Float getLongitude() {
        return longitude;
    }

    public static Float getTemperature() {
        return temperature;
    }
}
