package com.jeremy.algorithm;

import org.junit.Test;

public class PaternTest {

    @Test
    public void testProducerConsumerPatern(){
        ProducerConsumerPatern producerConsumerPatern = new ProducerConsumerPatern();

//        new Thread(producerConsumerPatern.new Producer()).start();
//        new Thread(producerConsumerPatern.new Consumer()).start();
//        new Thread(producerConsumerPatern.new Producer()).start();
//        new Thread(producerConsumerPatern.new Consumer()).start();
//        new Thread(producerConsumerPatern.new Producer()).start();
//        new Thread(producerConsumerPatern.new Consumer()).start();

//        new Thread(producerConsumerPatern.new ProducerByLock()).start();
//        new Thread(producerConsumerPatern.new ConsumerByLock()).start();
//        new Thread(producerConsumerPatern.new ProducerByLock()).start();
//        new Thread(producerConsumerPatern.new ConsumerByLock()).start();
//        new Thread(producerConsumerPatern.new ProducerByLock()).start();
//        new Thread(producerConsumerPatern.new ConsumerByLock()).start();

        new Thread(producerConsumerPatern.new ProducerByBQ()).start();
        new Thread(producerConsumerPatern.new ConsumerByBQ()).start();
        new Thread(producerConsumerPatern.new ProducerByBQ()).start();
        new Thread(producerConsumerPatern.new ConsumerByBQ()).start();
        new Thread(producerConsumerPatern.new ProducerByBQ()).start();
        new Thread(producerConsumerPatern.new ConsumerByBQ()).start();


        try {
            Thread.sleep(5000);
        }catch (Exception e){

        }

    }

}
