package com.epam.taks5;

public interface ProducerConsumerQueue<T> {
    T get() throws InterruptedException;
    void put(T t) throws InterruptedException;
}
