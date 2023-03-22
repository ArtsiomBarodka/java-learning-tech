package com.epam.task6.doubleSquares;

import java.util.Arrays;

public class LinearArrayCounter extends ArrayCounter {
    @Override
    protected double getSumOfSquares(double[] array) {
        return Arrays.stream(array).map(item -> item * item).sum();
    }
}
