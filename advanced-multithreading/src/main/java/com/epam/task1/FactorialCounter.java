package com.epam.task1;

import java.math.BigInteger;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;

public class FactorialCounter {
    private static final Function<BigInteger, BigInteger> SEQUENTIAL_FACTORIAL_COUNTER = FactorialCounter::getFactorial;

    private static final Function<BigInteger, BigInteger> FJP_FACTORIAL_COUNTER = (n) -> {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        return forkJoinPool.invoke(new FactorialRecursiveTask(n));
    };

    public static void main(String[] args) {
        final int first_number = 10;
        final int second_number = 100;
        final int third_number = 1000;

        System.out.printf("First: Number = %d \n", first_number);

        System.out.println("Counting by FJP: ");
        executeFactorialCounter(first_number, FJP_FACTORIAL_COUNTER);
        System.out.println("Counting by SEQUENTIAL: ");
        executeFactorialCounter(first_number, SEQUENTIAL_FACTORIAL_COUNTER);
        System.out.println("============================================= \n");


        System.out.printf("Second: Number = %d \n", second_number);

        System.out.println("Counting by FJP: ");
        executeFactorialCounter(second_number, FJP_FACTORIAL_COUNTER);
        System.out.println("Counting by SEQUENTIAL: ");
        executeFactorialCounter(second_number, SEQUENTIAL_FACTORIAL_COUNTER);
        System.out.println("============================================= \n");


        System.out.printf("Third: Number = %d \n", third_number);

        System.out.println("Counting by FJP: ");
        executeFactorialCounter(third_number, FJP_FACTORIAL_COUNTER);
        System.out.println("Counting by SEQUENTIAL: ");
        executeFactorialCounter(third_number, SEQUENTIAL_FACTORIAL_COUNTER);
        System.out.println("============================================= \n");
    }

    private static void executeFactorialCounter(int number, Function<BigInteger, BigInteger> counter) {
        var convertedNumber = BigInteger.valueOf(number);

        long start = System.nanoTime();
        var result = counter.apply(convertedNumber);
        long end = System.nanoTime();

        System.out.printf("Factorial of %d number: %s \n", number, result);
        System.out.printf("The time of execution: %d \n", end - start);
    }


    private static BigInteger getFactorial(BigInteger number) {
        if (number.compareTo(BigInteger.TWO) <= 0) {
            return number;
        } else {
            return number.multiply(getFactorial(number.subtract(BigInteger.ONE)));
        }
    }
}
