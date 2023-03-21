package com.epam.task6.doubleSquares;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;

public class Main {
    public static void main(String[] args) {
        final LinearArrayCounter linearArrayCounter = new LinearArrayCounter();
        final FJPArrayCounter fjpArrayCounter = new FJPArrayCounter();

        double[] doubles = generateArray(500_000_000L);

        System.out.println("Count via linear approach");
        linearArrayCounter.countSumOfSquares(doubles);
        System.out.println("==================================");

        System.out.println("Count via fjp approach");
        fjpArrayCounter.countSumOfSquares(doubles);
        System.out.println("==================================");
    }

    private static double[] generateArray(long size) {
        return DoubleStream
                .generate(ThreadLocalRandom.current()::nextDouble)
                .limit(size)
                .toArray();
    }
}
