package com.epam.task1;

import java.math.BigInteger;
import java.util.concurrent.ForkJoinPool;

public final class FJPFactorialCounter extends FactorialCounter<BigInteger> {
    private final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    @Override
    protected BigInteger getFactorial(BigInteger number) {
        return forkJoinPool.invoke(new FactorialRecursiveTask(number));
    }
}
