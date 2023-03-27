package com.epam.task6.doubleSquares;

public abstract class ArrayCounter {
    protected abstract double getSumOfSquares(double[] array);

    public void countSumOfSquares(double[] array) {
        long start = System.nanoTime();
        var result = getSumOfSquares(array);
        long end = System.nanoTime();

        System.out.printf("sum of arrays items squares: %s \n", result);
        System.out.printf("The time of execution: %d \n", end - start);
    }
}
