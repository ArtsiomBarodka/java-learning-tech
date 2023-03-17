package com.epam.taks5;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueBasedQueue<T> implements ProducerConsumerQueue<T> {
    private final BlockingQueue<T> queue;

    public BlockingQueueBasedQueue(int capacity) {
        this.queue = new ArrayBlockingQueue<>(capacity);
    }

    @Override
    public T get() throws InterruptedException {
        return queue.take();
    }

    @Override
    public void put(T t) throws InterruptedException {
        queue.put(t);
    }
}
