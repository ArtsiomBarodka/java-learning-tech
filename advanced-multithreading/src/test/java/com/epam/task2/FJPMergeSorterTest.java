package com.epam.task2;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FJPMergeSorterTest {
    private final MergeSorter sorter = new FJPMergeSorter();

    @Test
    void sort(){
        int[] arr = getArray(10000000);

        sorter.sort(arr);

        assertTrue(isSorted(arr), ()-> "Array is sorted");
    }

    private int[] getArray (int length) {
        return IntStream.range(0, length)
                .map(i -> ThreadLocalRandom.current().nextInt())
                .toArray();
    }

    private boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1])
                return false;
        }
        return true;
    }
}
