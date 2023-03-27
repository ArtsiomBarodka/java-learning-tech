package com.epam.taks5;

public final class StringConsumer extends Consumer<String> {
    private final int count;

    public StringConsumer(ProducerConsumerQueue<String> producerConsumerQueue, int count) {
        super(producerConsumerQueue);
        this.count = count;
    }

    @Override
    public void run() {
        for (int i = 0; i < count; i++) {
            var item = consume();
            System.out.printf("Consumer %s: retrieved item %d : %s \n", Thread.currentThread().getName(), i, item);
        }
    }
}
