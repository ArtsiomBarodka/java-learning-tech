package com.epam.taks5;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class SemaphoreBasedQueue<T> implements ProducerConsumerQueue<T> {
    private final Semaphore producer;
    private final Semaphore consumer;
    private final Queue<T> queue;

    private final int capacity;

    public SemaphoreBasedQueue(int capacity) {
        this.producer = new Semaphore(1);
        this.consumer = new Semaphore(1);
        this.queue = new ArrayDeque<>(capacity);
        this.capacity = capacity;
    }

    @Override
    public T get() throws InterruptedException {
        consumer.acquire();

        T item;
        do {
            item = queue.poll();
        } while (item == null);

        consumer.release();
        return item;
    }

    @Override
    public void put(T item) throws InterruptedException {
        producer.acquire();

        boolean isAdded = false;
        do {
            if(capacity > queue.size()){
                isAdded = queue.offer(item);
            }
        } while (!isAdded);

        producer.release();
    }
}
