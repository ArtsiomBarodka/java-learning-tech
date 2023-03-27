package com.epam.task6.fibonacci;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FJPFibonacciCounterTest {
    private final FibonacciCounter fibonacciCounter = new FJPFibonacciCounter();

    @Test
    void countTest() {
        assertEquals(1134903170L, fibonacciCounter.count(45));
    }

    @Test
    void countTest_lessThen1() {
        assertEquals(1, fibonacciCounter.count(1));
    }

    @Test
    void countTest_lessThen10() {
        assertEquals(34, fibonacciCounter.count(9));
    }
}
