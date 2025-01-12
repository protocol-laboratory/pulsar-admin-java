package io.github.protocol.pulsar.admin.jdk;

import java.util.Random;
import java.util.UUID;

public class RandomUtil {

    public static Random random = new Random();

    public static String randomString() {
        return UUID.randomUUID().toString();
    }

    public static int randomInt() {
        return random.nextInt();
    }

    public static long randomLong() {
        return random.nextLong();
    }

    public static long randomPositiveLong() {
        return Math.abs(random.nextLong());
    }

    public static int randomPositiveInt() {
        return random.nextInt(Integer.MAX_VALUE);
    }

    public static int randomNegativeInt() {
        return -random.nextInt(Integer.MAX_VALUE);
    }

}
