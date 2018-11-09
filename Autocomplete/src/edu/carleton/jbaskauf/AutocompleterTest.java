package edu.carleton.jbaskauf;

/** AutocompleterTest.java
 * @author Jessie Baskauf and Ellie Mamantov
 * @version 04/05/2018
 *
 * Unit tests for the getCompletions method of Autocompleter.java
 */

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AutocompleterTest {

    private Autocompleter autocompleter;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        autocompleter = new Autocompleter("actors.txt");
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        autocompleter = null;
    }

    @org.junit.jupiter.api.Test
    void completeEmptyString() {
        List<String> completions = autocompleter.getCompletions("");
        assertEquals(0, completions.size(), "Empty string generated one or more completions.");
    }

    @org.junit.jupiter.api.Test
    //Assumes that getCompletions() creates an empty List for a null search string rather than returning null.
    void nullString() {
        List<String> completions = autocompleter.getCompletions(null);
        assertEquals(0, completions.size(), "Null string generated one or more completions.");
    }

    @org.junit.jupiter.api.Test
    void fullName() {
        List<String> completions = autocompleter.getCompletions("Goldberg, Whoopi");
        String[] expected = {"Goldberg, Whoopi"};
        assertArrayEquals(expected, completions.toArray(), "Did not return a single match for a unique full name.");
    }

    @org.junit.jupiter.api.Test
    void lowerCaseFullName() {
        List<String> completions = autocompleter.getCompletions("goldberg,whoopi");
        String[] expected = {"Goldberg, Whoopi"};
        assertArrayEquals(expected, completions.toArray(), "Did not return match for a lowercase full name.");
    }

    @org.junit.jupiter.api.Test
    void invalidInput() {
        List<String> completions = autocompleter.getCompletions("whoopi,goldberg");
        String[] expected = {};
        assertArrayEquals(expected, completions.toArray(), "Incorrectly found a match for an invalid input.");
    }

    @org.junit.jupiter.api.Test
    void fullFirstName() {
        List<String> completions = autocompleter.getCompletions("mark");
        String[] expected = {"Hamill, Mark", "Strong, Mark", "Ruffalo, Mark", "Rylance, Mark", "Wahlberg, Mark"};
        assertArrayEquals(expected, completions.toArray(), "Did not correctly find or sort matches for a full first name.");
    }

    @org.junit.jupiter.api.Test
    void fullLastName() {
        List<String> completions = autocompleter.getCompletions("cooper");
        String[] expected = {"Cooper, Bradley", "Cooper, Chris", "Cooper, Gary"};
        assertArrayEquals(expected, completions.toArray(), "Did not correctly find or sort matches for a full last name.");
    }

    @org.junit.jupiter.api.Test
    void partialFirstNameBeginning() {
        List<String> completions = autocompleter.getCompletions("kev");
        String[] expected = {"Hart, Kevin", "Bacon, Kevin", "James, Kevin", "Kline, Kevin", "Spacey, Kevin", "Costner, Kevin"};
        assertArrayEquals(expected, completions.toArray(), "Did not correctly find or sort partial matches for the beginning of a first name.");
    }

    @org.junit.jupiter.api.Test
    void partialLastNameBeginning() {
        List<String> completions = autocompleter.getCompletions("winst");
        String[] expected = {"Winstead, Mary Elizabeth", "Winstone, Ray"};
        assertArrayEquals(expected, completions.toArray(), "Did not correctly find or sort partial matches for the beginning of a last name.");
    }

    @org.junit.jupiter.api.Test
    void partialFirstNameNotBeginning() {
        List<String> completions = autocompleter.getCompletions("illian");
        String[] expected = {"Gish, Lillian", "Murphy, Cillian", "Anderson, Gillian"};
        assertArrayEquals(expected, completions.toArray(), "Did not correctly find or sort partial matches for a string not located at the beginning of the first name.");
    }

    @org.junit.jupiter.api.Test
    void partialLastNameNotBeginning() {
        List<String> completions = autocompleter.getCompletions("ook");
        String[] expected = {"Rooker, Michael", "Brooks, Albert", "Holbrook, Hal"};
        assertArrayEquals(expected, completions.toArray(), "Did not correctly find or sort partial matches for a string not located at the beginning of the last name.");
    }

    @org.junit.jupiter.api.Test
    void acrossCommaBoundary() {
        List<String> completions = autocompleter.getCompletions("en, w");
        String[] expected = {"Allen, Woody", "Holden, William"};
        assertArrayEquals(expected, completions.toArray(), "Did not correctly find or sort partial matches across the comma boundary.");
    }

    @org.junit.jupiter.api.Test
    void firstOrLast() {
        List<String> completions = autocompleter.getCompletions("lac");
        String[] expected = {"Black, Jack", "Beery, Wallace"};
        assertArrayEquals(expected, completions.toArray(), "Did not correctly find or sort partial matches found in both a first and last name.");
    }

    @org.junit.jupiter.api.Test
    void singleLastName() {
        List<String> completions = autocompleter.getCompletions("cher");
        String[] expected = {"Cher", "Hutcherson, Josh", "Kutcher, Ashton", "Fletcher, Louise"};
        assertArrayEquals(expected, completions.toArray(), "Did not correctly find or sort partial matches found in first or last name or across comma.");
    }

    @org.junit.jupiter.api.Test
    void beginningOrMiddleOfName() {
        List<String> completions = autocompleter.getCompletions("ada");
        String[] expected = {"Adams, Amy", "Sandler, Adam", "McAdams, Rachel", "Smith, Jada Pinkett"};
        assertArrayEquals(expected, completions.toArray(), "Did not correctly find or sort partial matches found at beginning or middle of a name.");
    }
}