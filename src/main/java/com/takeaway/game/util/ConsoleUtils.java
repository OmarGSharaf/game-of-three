package com.takeaway.game.util;

import org.springframework.util.StringUtils;

import java.util.Scanner;

public class ConsoleUtils {

    public static final String PRESS_ENTER_KEY_TO_CONTINUE = "Press Enter key to continue...";

    public static void pressEnterKeyToContinue() {
        System.out.println(PRESS_ENTER_KEY_TO_CONTINUE);
        try {
            System.in.read();
        } catch (Exception ignored) {
        }
    }

    public static String scan(String placeholder) {
        if (!StringUtils.isEmpty(placeholder)) {
            System.out.println(placeholder);
        }
        return scan();
    }

    public static String scan() {
        return new Scanner(System.in).next();
    }

    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
