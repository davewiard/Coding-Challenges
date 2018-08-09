# Cayuse Code Challenge

## Assignment

Create a utility using the language/tools of your choice that takes a
ZIP-code, then outputs the city name, current temperature, time zone,
and general elevation at the location with a user-friendly message. For
example, "At the location $CITY_NAME, the temperature is $TEMPERATURE,
the timezone is $TIMEZONE, and the elevation is $ELEVATION".

Use the Open WeatherMap current weather API to retrieve the current
temperature and city name. You will be required to sign up for a free
API key.

Use the Google Time Zone API to get the current timezone for a location.
You will again need to register a “project” and sign up for a free API
key* with Google.

Use the Google Elevation API to retrieve elevation data for a location.

\* Note that a credit card will soon be required to register for an API
key with Google (though the first 40k API calls are still free). If this
is a blocker, please contact Nick Seibert for a temporary API key.

## Assistance and completion

Please complete your assignment by your interview date. You’re free to
email a ZIP/tar file, use GitHub, Plunker, etc., or just bring a thumb
drive with your source code. Questions are welcome, just contact
nick.seibert@cayuse.com for any clarifications or to turn in your
assignment.

### Backend developers

Your utility can accept ZIP-code as a command line parameter and simply
write its output to the console/STDOUT/STDERR once retrieved. Node or
Java are preferred but not required.

### Frontend developers

Create an HTML page which allows input of the ZIP-code, and writes the
required output message to the page once it is retrieved. Backbone or
Marionette are preferred but not required.

Note that Open WeatherMap will not allow CORS requests from any location
directly from the browser, so we’ve made the following backend proxy
available for you to use.

In place of “http://api.openweathermap.org/data/2.5...”, substitute:
https://api.develop.apps.cayuse.com/testproxy/data/2.5...

The same is true for Google APIs, so use the following proxy when
calling directly from the browser:

In place of “https://maps.googleapis.com/maps...”, substitute:
https://api.develop.apps.cayuse.com/testproxy/maps…

### Full-stack developers

Feel free to pick one assignment or the other (either frontend or
backend). If you’re feeling ambitious and have time, try to complete the
frontend assignment without using the provided proxies.


## Solution

### Architectural Design

To avoid the chance of my private API keys being stored in a public
GitHub repository I decided to require the keys be stored in environment
variables on the build machine. This is not a secure way to store keys
but for the purposes of this code challenge it should suffice. Required
environment variables are:

    API_KEY_GOOGLE='<YOUR Google API KEY HERE>'
    API_KEY_OWM='<YOUR OpenWeatherMap API KEY HERE>'

In IntelliJ IDEA you will need to edit the Main configuration. Click the
folder icon to the right of the "Environment Variables" field and fill
in the values for both with your own API keys.

### Runtime Overview

The app first gets the API keys from the environment and configures the
Google Geo API context. The same Google API context is used with both
the TimeZone and Elevation APIs per Google's recommendation.

The app then reads the ZIP codes from either the command-line arguments
(if any) or from stdin (if no command-line arguments provided). The
assignment specified to take "a ZIP-code" which I read to mean "one ZIP
code" but I took a small liberty and made this app accept any number of
ZIP codes (up to the command-line length limit which varies).

The app then loops through the list of ZIP codes performing the
following steps for each ZIP code

1. Requests the OpenWeatherMap data for the current ZIP code
2. From the results, the city name, latitude, and longitude are saved for
later use
3. Passes the saved latitude and longitude into the Google TimeZone and
Elevation APIs and takes the timezone and elevation from the results
4. Writes the requested output string to STDOUT

### Classes

I created four separate classes for this challenge.

#### City Data

The CityData class holds the data we want to get from OpenWeatherMap and
Google APIs. For this code challenge the class is a bit overkill but if
we wanted to further manipulate or use the data beyond just printing it
to STDOUT we'd want to expand this class.

Because our ZipCodeValidator class accepts ZIP+4 formatted input we need
to strip off the +4 portion before we can use the ZIP code with the
OpenWeatherMap API. The OpenWeatherMap API does not support ZIP+4 which
makes sense. For getting weather-related data the post office box number
is irrelevant.

#### GoogleMapsApi

This class encapsulates the "dirty work" of requesting data from the two
Google APIs. I chose to make this class final with a private constructor
to prevent instantiation of GoogleMapsApi objects. This is not necessary
because all of the functionality within this class can be referenced
directly through a class reference.

#### OpenWeatherMap

This class sets up the "requester" for OpenWeatherMap in the constructor
and then requires the caller to invoke the getOpenWeatherMapData method.
This method retrieves the data and makes the city name, temperature,
latitude, and longitude values available to the caller.

#### ZipCodeValidator

A class to verify if a given string represents a valid ZIP code or
ZIP+4. It does not validate whether a given ZIP code or ZIP+4 is
actually a valid ZIP code, just that it is in a specific format. For
instance, 98116 is a valid ZIP code for Seattle but 11111 is not a valid
ZIP code. Both are formatted properly so ZipCodeValidator will return
true but requesting OpenWeatherMap data for ZIP code "00000" will
generate an exception.

### Unit Tests

Due to the complexity and overhead of setting up a mocking environment
(such as Mockito) to mock the input and output objects of the APIs I
have chosen not to implement unit tests for much of this code challenge.
I did make some unit tests for the ZipCodeValidator class, though.
Another challenge to running unit tests is the fact that several of the
class members are private static fields which increases the difficulty
a bit.

#### ZipCodeValidatorTest

This set of unit tests checks whether the isValidZipCode() method is
returning the correct value for different inputs, both valid and
invalid.
