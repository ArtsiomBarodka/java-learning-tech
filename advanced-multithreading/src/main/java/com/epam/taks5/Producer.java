package com.epam.taks5;

public abstract class Producer<T> implements Runnable {
    private final ProducerConsumerQueue<T> producerConsumerQueue;

    public Producer(ProducerConsumerQueue<T> producerConsumerQueue) {
        this.producerConsumerQueue = producerConsumerQueue;
    }

    protected void produce(T item) {
        try {
            Thread.sleep(1000);
            producerConsumerQueue.put(item);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
