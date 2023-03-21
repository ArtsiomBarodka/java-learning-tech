package com.epam.task6.fibonacci;

import java.util.concurrent.RecursiveTask;

public class FibonacciTask extends RecursiveTask<Long> {
    final int n;

    FibonacciTask(int n) {
        this.n = n;
    }

    @Override
    protected Long compute() {
        if (n <= 1) {
            return (long) n;
        }

        if (n <= 10) {
            return countLinearly();
        }

        var f1 = new FibonacciTask(n - 1);
        f1.fork();
        var f2 = new FibonacciTask(n - 2);

        return f2.compute() + f1.join();
    }

    private Long countLinearly() {
        long firstTerm = 0;
        long secondTerm = 1;
        long result = 0;

        for (int i = 1; i < n; ++i) {
            // compute the next term
            result = firstTerm + secondTerm;
            firstTerm = secondTerm;
            secondTerm = result;
        }

        return result;
    }
}