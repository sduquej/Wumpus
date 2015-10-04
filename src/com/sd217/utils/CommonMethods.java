package com.sd217.utils;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Sebastián Duque on 02/10/15.
 * This class contains helper methods that are might be needed by
 * other parts of the game
 */
public class CommonMethods {
    /**
     * Shuffles the contents of an array using See <a href="https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle">Fisher–Yates shuffle</a>
     * Taken and adapted from: http://stackoverflow.com/questions/1519736/random-shuffling-of-an-array
     * @param array Object array to be shuffled
     */
    public static void shuffleArray(Object[] array){
        int index;
        Object temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    /**
     * Returns a pseudorandom integer value
     * @param min Lower bound, inclusive.
     * @param max Upper bound, exclusive.
     * @return Random integer value in the given range.
     */
    public static int getRandomInt(int min, int max){
        return ThreadLocalRandom.current().nextInt(min, max);
    }
}
