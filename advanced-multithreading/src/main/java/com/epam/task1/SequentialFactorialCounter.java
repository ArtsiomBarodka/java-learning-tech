package com.epam.task1;

import java.math.BigInteger;

public final class SequentialFactorialCounter extends FactorialCounter<BigInteger> {
    @Override
    protected BigInteger getFactorial(BigInteger number) {
        if (number.compareTo(BigInteger.TWO) <= 0) {
            return number;
        } else {
            return number.multiply(getFactorial(number.subtract(BigInteger.ONE)));
        }
    }
}
