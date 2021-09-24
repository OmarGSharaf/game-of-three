package com.takeaway.game.handler;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class AutomaticGameHandler implements GameHandler {

    Random generator = new Random();
    int[] options = {-1, 0, 1};

    @Override
    public int init() {
        return generator.nextInt(100);
    }

    @Override
    public int next(int value) {
        return (value + options[generator.nextInt(options.length)]) / 3;
    }
}
