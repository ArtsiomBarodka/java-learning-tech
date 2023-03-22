package com.epam.task1;

import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
        final BigInteger first_number = BigInteger.valueOf(10);
        final BigInteger second_number = BigInteger.valueOf(100);
        final BigInteger third_number = BigInteger.valueOf(1000);

        final FJPFactorialCounter fjpFactorialCounter = new FJPFactorialCounter();
        final SequentialFactorialCounter sequentialFactorialCounter = new SequentialFactorialCounter();

        fjpFactorialCounter.countFactorial(BigInteger.valueOf(1000));
        sequentialFactorialCounter.countFactorial(BigInteger.valueOf(1000));

        System.out.printf("First: Number = %d \n", first_number);

        System.out.println("Counting by FJP: ");
        fjpFactorialCounter.countFactorial(first_number);
        System.out.println("Counting by SEQUENTIAL: ");
        sequentialFactorialCounter.countFactorial(first_number);
        System.out.println("============================================= \n");


        System.out.printf("Second: Number = %d \n", second_number);

        System.out.println("Counting by FJP: ");
        fjpFactorialCounter.countFactorial(second_number);
        System.out.println("Counting by SEQUENTIAL: ");
        sequentialFactorialCounter.countFactorial(second_number);
        System.out.println("============================================= \n");


        System.out.printf("Third: Number = %d \n", third_number);

        System.out.println("Counting by FJP: ");
        fjpFactorialCounter.countFactorial(third_number);
        System.out.println("Counting by SEQUENTIAL: ");
        sequentialFactorialCounter.countFactorial(third_number);
        System.out.println("============================================= \n");
    }
}
