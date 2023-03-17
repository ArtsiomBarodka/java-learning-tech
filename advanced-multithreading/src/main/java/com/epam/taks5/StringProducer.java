package com.epam.taks5;

public final class StringProducer extends Producer<String> {
    private final int count;

    public StringProducer(ProducerConsumerQueue<String> producerConsumerQueue, int count) {
        super(producerConsumerQueue);
        this.count = count;
    }

    @Override
    public void run() {
        for (int i = 0; i < count; i++) {
            String item = String.valueOf(i);
            produce(item);
            System.out.printf("Producer %s: produce item %d : %s \n", Thread.currentThread().getName(), i, item);
        }
    }
}
