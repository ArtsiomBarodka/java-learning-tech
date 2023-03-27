package com.epam.task1;

public abstract class FactorialCounter<T extends Number> {
    protected abstract T getFactorial(T number);

    public void countFactorial(T number) {
        long start = System.nanoTime();
        var result = getFactorial(number);
        long end = System.nanoTime();

        System.out.printf("Factorial of %d number: %s \n", number, result);
        System.out.printf("The time of execution: %d \n", end - start);
    }
}
