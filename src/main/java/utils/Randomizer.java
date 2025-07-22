package utils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class Randomizer {

    private Randomizer() {}

    public static boolean getProbability(double percent) {
        return ThreadLocalRandom.current().nextDouble(100) < percent;
    }

    public static int nextInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public static int nextInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }

    public static <T> T randomItem(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Список не может быть пустым");
        }
        return list.get(nextInt(list.size()));
    }

    public static <T extends Enum<T>> T randomEnum(Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();
        return values[nextInt(values.length)];
    }

    public static double nextDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }
}