package com.epam.task6.doubleSquares;

import java.util.concurrent.ForkJoinPool;

public final class FJPArrayCounter extends ArrayCounter {
    private final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    @Override
    protected double getSumOfSquares(double[] array) {
        var sumOfSquaresAction = new SumOfSquaresAction(array, 0, array.length, null);

        forkJoinPool.invoke(sumOfSquaresAction);

        return sumOfSquaresAction.getResult();
    }
}
