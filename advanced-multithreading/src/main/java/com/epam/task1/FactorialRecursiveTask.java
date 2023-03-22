package com.epam.task1;

import java.math.BigInteger;
import java.util.concurrent.RecursiveTask;

public class FactorialRecursiveTask extends RecursiveTask<BigInteger> {
    private final BigInteger number;

    public FactorialRecursiveTask(BigInteger number) {
        this.number = number;
    }

    @Override
    protected BigInteger compute() {
        if (number.compareTo(BigInteger.TWO) <= 0) {
            return number;
        } else {
            final var factorialRecursiveSubTask = new FactorialRecursiveTask(number.subtract(BigInteger.ONE));
            factorialRecursiveSubTask.fork();
            return number.multiply(factorialRecursiveSubTask.join());
        }
    }
}
