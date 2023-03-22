package com.epam.taks5;

public class Main {
    public static void main(String[] args) {
        int capacity = 2;

        var stringSemaphoreQueue = new SemaphoreBasedQueue<String>(capacity);
        var stringBlockingQueue = new BlockingQueueBasedQueue<String>(capacity);

        int prodConsItemsCount = 10;
        var stringProducer1 = new StringProducer(stringSemaphoreQueue, prodConsItemsCount);
        var stringConsumer1 = new StringConsumer(stringSemaphoreQueue, prodConsItemsCount);
        var stringProducer2 = new StringProducer(stringBlockingQueue, prodConsItemsCount);
        var stringConsumer2 = new StringConsumer(stringBlockingQueue, prodConsItemsCount);


        var threadP1 = new Thread(stringProducer1);
        var threadC1 = new Thread(stringConsumer1);

        var threadP2 = new Thread(stringProducer2);
        var threadC2 = new Thread(stringConsumer2);

        threadP1.start();
        threadC1.start();

        threadP2.start();
        threadC2.start();
    }
}
