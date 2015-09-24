package com.sd217;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by sd217 on 24/09/15.
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
}
