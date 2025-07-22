package statistics;

import model.animals.Animal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Класс для сбора и отображения статистики симуляции.
 */
public class Statistics {
    private static final AtomicInteger totalDeathsByHunger = new AtomicInteger(0);
    private static final AtomicInteger totalDeathsByPredation = new AtomicInteger(0);
    private static final AtomicInteger deathsByHungerToday = new AtomicInteger(0);
    private static final AtomicInteger totalBirths = new AtomicInteger(0);
    private static final AtomicInteger totalPlants = new AtomicInteger(0);

    private static final Map<Class<? extends Animal>, AtomicInteger> currentPopulation = new ConcurrentHashMap<>();

    // === Регистрация событий ===

    public static void recordDeathByHunger(Animal animal) {
        totalDeathsByHunger.incrementAndGet();
        deathsByHungerToday.incrementAndGet();
        decreasePopulation(animal);
    }

    public static void recordDeathByPredation(Animal animal) {
        totalDeathsByPredation.incrementAndGet();
        decreasePopulation(animal);
    }

    public static void recordBirth(Animal animal) {
        totalBirths.incrementAndGet();
        increasePopulation(animal);
    }

    public static void updatePlants(int total) {
        totalPlants.set(total);
    }

    public static void increasePopulation(Animal animal) {
        currentPopulation
                .computeIfAbsent(animal.getClass(), k -> new AtomicInteger(0))
                .incrementAndGet();
    }

    public static void decreasePopulation(Animal animal) {
        currentPopulation
                .computeIfAbsent(animal.getClass(), k -> new AtomicInteger(0))
                .decrementAndGet();
    }

    public static void resetDailyHungerDeaths() {
        deathsByHungerToday.set(0);
    }

    public static int getDeathsByHungerCount() {
        return deathsByHungerToday.get();
    }

    // === Вывод статистики ===

    public static void printStatistics() {
        System.out.println("\n📊 === СТАТИСТИКА ===");
        System.out.println("🌿 Растений на острове : " + totalPlants.get());
        System.out.println("💀 Смертей от голода    : " + totalDeathsByHunger.get());
        System.out.println("🩸 Смертей от хищников  : " + totalDeathsByPredation.get());
        System.out.println("🐣 Всего рождений       : " + totalBirths.get());
        System.out.println("📋 Популяция животных:");

        for (Map.Entry<Class<? extends Animal>, AtomicInteger> entry : currentPopulation.entrySet()) {
            Class<? extends Animal> clazz = entry.getKey();
            int count = entry.getValue().get();
            String emoji = "?";
            String name = clazz.getSimpleName();

            try {
                Animal animal = clazz.getDeclaredConstructor().newInstance();
                emoji = animal.getEmoji();
            } catch (Exception ignored) {}

            String displayCount = (count <= 0) ? "Вымерли" : String.valueOf(count);
            System.out.printf("   %s %-12s : %s%n", emoji, name, displayCount);
        }

        System.out.println("========================\n");
    }
}
