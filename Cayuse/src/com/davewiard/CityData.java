package com.davewiard;

/**
 * This implementation of CityData class is relatively simple and functions somewhat similarly to a C/C++ struct. It
 * it just housing related data in a single class structure. Since we are only printing this data to the screen at the
 * end there is no significant reason to do this other than to demonstrate that if we did need to further process the
 * data we would have it all here instead of scattered about in objects returned from a library reading API data.
 *
 * There are no JUnit tests for this class because this class only contains trivial getters and setters and there is
 * no intrinsic value to testing these with JUnit tests.
 */
public class CityData {
    private String zipCode;
    private Float latitude;
    private Float longitude;

    private String cityName;
    private String timeZone;
    private Double elevation;
    private Float temperature;

    public CityData(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getZipCode() {
        if (this.zipCode.contains("-")) {
            return zipCode.substring(0, zipCode.indexOf('-'));
        }

        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Double getElevation() {
        return elevation;
    }

    public void setElevation(Double elevation) {
        this.elevation = elevation;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

}
