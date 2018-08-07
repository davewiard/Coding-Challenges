package com.davewiard;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ZipCodeValidatorTest {

    private ZipCodeValidator zipCodeValidator;

    /**
     * Before each method marked with @Test is run create a new ZipCodeValidator
     * object to operate on.
     */
    @BeforeEach
    public void setUp() {
        zipCodeValidator = new ZipCodeValidator();
    }

    /**
     * What to do after each method marked with @Test is run
     */
    @AfterEach
    public void tearDown() {
    }

    /**
     * Tests if a 5-digits ZIP code is valid
     */
    @Test
    public void ValidZipCodeTest1() {
        System.out.println("ZIP code 12345 is valid: " + zipCodeValidator.isValidZipCode("12345"));
        assertTrue(zipCodeValidator.isValidZipCode("12345"));
    }

    /**
     * Tests if a ZIP+4 is valid
     */
    @Test
    public void ValidZipCodeTest2() {
        System.out.println("ZIP code 12345-6789 is valid: " + zipCodeValidator.isValidZipCode("12345-6789"));
        assertTrue(zipCodeValidator.isValidZipCode("12345-6789"));
    }

    /**
     * Tests if an invalid ZIP containing a letter is valid
     */
    @Test
    public void InvalidZipCodeTest1() {
        System.out.println("ZIP code A4321 is valid: " + zipCodeValidator.isValidZipCode("A4321"));
        assertFalse(zipCodeValidator.isValidZipCode("A4321"));
    }

    /**
     * Tests if an invalid ZIP+5 is valid
     */
    @Test
    public void InvalidZipCodeTest2() {
        System.out.println("ZIP code 54321-06789 is valid: " + zipCodeValidator.isValidZipCode("54321-06789"));
        assertFalse(zipCodeValidator.isValidZipCode("54321-06789"));
    }

    /**
     * Tests if an invalid ZIP+4 containing a letter in the +4 is valid
     */
    @Test
    public void InvalidZipCodeTest3() {
        System.out.println("ZIP code 12345-678d is valid: " + zipCodeValidator.isValidZipCode("12345-678d"));
        assertFalse(zipCodeValidator.isValidZipCode("12345-678d"));
    }

    /**
     * Tests if an invalid ZIP+4 containing only letters in the +4 is valid
     */
    @Test
    public void InvalidZipCodeTest4() {
        System.out.println("ZIP code 12345-abcd is valid: " + zipCodeValidator.isValidZipCode("12345-abcd"));
        assertFalse(zipCodeValidator.isValidZipCode("12345-abcd"));
    }
}