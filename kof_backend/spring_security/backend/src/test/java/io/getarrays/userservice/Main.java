package io.getarrays.userservice;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random rand = new Random(); //instance of random class
        int upperbound = 25;
        //generate random values from 0-24
        int int_random = rand.nextInt(upperbound);

        System.out.println("Random integer value from 0 to" + (upperbound-1) + " : "+ int_random);
    }
}
