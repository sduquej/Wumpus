package com.sd217.utils;

import com.sd217.exceptions.InvalidActionException;
import com.sd217.exceptions.InvalidDirectionException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by Sebastián Duque on 24/09/15.
 * This class contains helper methods to read and perform validation on
 * input from the console.
 */
public class ConsoleReader {
    private static BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Reads from the console an integer and checks if it's between the min and max given values
     *
     * @param min minimum value accepted
     * @param max maximum value accepted
     */
    public static int readInt(int min, int max) {
        int readValue = Integer.MIN_VALUE;
        boolean validInput = false;

        while (!validInput) {
            try {
                readValue = Integer.parseInt(cin.readLine());
                if (readValue > min - 1 && readValue < max + 1) {
                    validInput = true;
                } else {
                    System.err.println("* it should be a number between " + min + " and " + max + " *");
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.err.println("* it should be a number between " + min + " and " + max + " *");
            } catch (NumberFormatException nfe) {
                System.err.println("* that's not a number *");
            }
        }

        return readValue;
    }

    /**
     * Reads from the console a character and checks if it's a valid action
     * @return String - Action chosen
     */
    public static String readAction(Map<Character, String> validActions) {
        return readValidCharacter(validActions, new InvalidActionException(validActions));
    }

    /**
     * Reads from the console a character and checks if it's a valid direction
     * @return String - Direction chosen
     */
    public static String readDirection(Map<Character, String> validDirections) {
        return readValidCharacter(validDirections, new InvalidDirectionException());
    }

    /**
     * Reads from the console and validates the first character read against they keys of the provided validValues Map.
     * @param validValues Map\<Character, String\> of valid values. The read character must be a key in this Map.
     * @param exception Exception to be thrown if the read character is not found in the map.
     * @return valid character, read from the console
     */
    private static String readValidCharacter(Map<Character, String> validValues, Exception exception) {
        char readValue = 0;
        boolean validInput = false;

        while (!validInput || readValue == 0) {
            try {
                readValue = cin.readLine().toUpperCase().charAt(0);
                if (validValues.get(readValue) != null) {
                    validInput = true;
                } else {
                    throw exception;
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.err.println(exception.getMessage());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

        return validValues.get(readValue);
    }
}
