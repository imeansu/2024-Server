package com.example.demo.common.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class RandomProviderImpl implements RandomProvider {

    public int getRandomInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }
}
