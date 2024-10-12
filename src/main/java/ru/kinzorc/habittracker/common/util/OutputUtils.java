package ru.kinzorc.habittracker.common.util;

import java.util.Random;

public class OutputUtils {
    private static final Random random = new Random();

    public static String generateResetCode() {
        return String.valueOf(100000 + random.nextInt(900000));
    }
}
