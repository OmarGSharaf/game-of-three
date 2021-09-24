package com.takeaway.game.handler;

import com.takeaway.game.util.ConsoleUtils;
import org.springframework.stereotype.Component;

@Component
public class ManualGameHandler implements GameHandler {

    @Override
    public int init() {
        System.out.println("\nEnter a positive number: ");
        while (true) {
            String str = ConsoleUtils.scan();
            if (isPositiveInteger(str)) {
                return Integer.parseInt(str);
            }
            System.out.println("Invalid input, please try again!\n");
        }
    }

    @Override
    public int next(int value) {
        return (value + selectOption(value)) / 3;
    }

    private int selectOption(int value) {
        System.out.printf("\nSelect one of the following numbers to add {-1, 0, 1} to %d: %n", value);
        while (true) {
            String str = ConsoleUtils.scan("");
            if (isValid(str)) {
                return Integer.parseInt(str);
            }
            System.out.println("Invalid input, please try again!\r");
        }
    }

    private boolean isValid(String str) {
        try {
            int i = Integer.parseInt(str);
            return i == -1 || i == 0 || i == 1;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isPositiveInteger(String str) {
        try {
            return Integer.parseInt(str) > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
