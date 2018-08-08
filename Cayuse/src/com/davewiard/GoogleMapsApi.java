package com.davewiard;

import com.google.maps.ElevationApi;
import com.google.maps.GeoApiContext;
import com.google.maps.TimeZoneApi;
import com.google.maps.model.ElevationResult;
import com.google.maps.model.LatLng;

import java.util.TimeZone;

final class GoogleMapsApi {

    /**
     * Prevent instantiation of this class. There is no need for it. Access the
     * getElevation() and getTimeZone() methods directly through a class reference.
     */
    private GoogleMapsApi() {}


    /**
     * Gets the elevation from the Google Elevation API
     * @param context Google's API context
     * @param latLng latitude and longitude coordinates to get the elevation for
     */
    static Double getElevation(GeoApiContext context, LatLng latLng) {
        ElevationResult result;

        try {
            // get the elevation for the given coordinates
            result = ElevationApi.getByPoint(context, latLng).await();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // TODO
        // This could potentially be rounded to 2 or 3 decimal places to make the result
        // more readable on the screen
        return result.elevation;
    }


    /**
     * Gets the timezone string value from the Google TimeZone API.
     * @param context Google's API context
     * @param latLng latitude and longitude coordinates to get the timezone for
     */
    static String getTimeZone(GeoApiContext context, LatLng latLng) {
        TimeZone timeZone;
        try {
            // get the timezone for the given coordinates
            timeZone = TimeZoneApi.getTimeZone(context, latLng).await();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return timeZone.getDisplayName();
    }

}
