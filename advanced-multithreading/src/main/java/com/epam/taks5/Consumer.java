package com.epam.taks5;

public abstract class Consumer<T> implements Runnable {
    private final ProducerConsumerQueue<T> producerConsumerQueue;

    public Consumer(ProducerConsumerQueue<T> producerConsumerQueue) {
        this.producerConsumerQueue = producerConsumerQueue;
    }

    protected T consume() {
        try {
            Thread.sleep(1000);
            return producerConsumerQueue.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
