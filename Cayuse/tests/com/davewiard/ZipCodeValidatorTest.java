package com.davewiard;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ZipCodeValidatorTest {

    /**
     * What to do before each method marked with @Test is run
     */
    @BeforeEach
    void setUp() { }

    /**
     * What to do after each method marked with @Test is run
     */
    @AfterEach
    void tearDown() { }

    /**
     * Tests if a 5-digits ZIP code is valid
     */
    @Test
    void ValidZipCodeTest1() {
        System.out.println("ZIP code 12345 is valid: " + ZipCodeValidator.isValidZipCode("12345"));
        assertTrue(ZipCodeValidator.isValidZipCode("12345"));
    }

    /**
     * Tests if a ZIP+4 is valid
     */
    @Test
    void ValidZipCodeTest2() {
        System.out.println("ZIP code 12345-6789 is valid: " + ZipCodeValidator.isValidZipCode("12345-6789"));
        assertTrue(ZipCodeValidator.isValidZipCode("12345-6789"));
    }

    /**
     * Tests if an invalid ZIP containing a letter is valid
     */
    @Test
    void InvalidZipCodeTest1() {
        System.out.println("ZIP code A4321 is valid: " + ZipCodeValidator.isValidZipCode("A4321"));
        assertFalse(ZipCodeValidator.isValidZipCode("A4321"));
    }

    /**
     * Tests if an invalid ZIP+5 is valid
     */
    @Test
    void InvalidZipCodeTest2() {
        System.out.println("ZIP code 54321-06789 is valid: " + ZipCodeValidator.isValidZipCode("54321-06789"));
        assertFalse(ZipCodeValidator.isValidZipCode("54321-06789"));
    }

    /**
     * Tests if an invalid ZIP+4 containing a letter in the +4 is valid
     */
    @Test
    void InvalidZipCodeTest3() {
        System.out.println("ZIP code 12345-678d is valid: " + ZipCodeValidator.isValidZipCode("12345-678d"));
        assertFalse(ZipCodeValidator.isValidZipCode("12345-678d"));
    }

    /**
     * Tests if an invalid ZIP+4 containing only letters in the +4 is valid
     */
    @Test
    void InvalidZipCodeTest4() {
        System.out.println("ZIP code 12345-abcd is valid: " + ZipCodeValidator.isValidZipCode("12345-abcd"));
        assertFalse(ZipCodeValidator.isValidZipCode("12345-abcd"));
    }
}
