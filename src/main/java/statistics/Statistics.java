package statistics;

import model.animals.Animal;

import java.util.Comparator;
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

    // Карта текущей популяции: Класс животного → Счётчик
    private static final Map<Class<? extends Animal>, AtomicInteger> animalPopulation = new ConcurrentHashMap<>();

    // Регистрирует появление животного (при рождении или изначальной генерации)
    public static void registerAnimal(Animal animal) {
        animalPopulation
                .computeIfAbsent(animal.getClass(), k -> new AtomicInteger(0))
                .incrementAndGet();
    }

    // Регистрирует смерть животного от голода
    public static void recordDeathByHunger(Animal animal) {
        totalDeathsByHunger.incrementAndGet();
        deathsByHungerToday.incrementAndGet();
        decrementAnimalCount(animal);
    }

    // Регистрирует смерть животного от хищника
    public static void recordDeathByPredation(Animal animal) {
        totalDeathsByPredation.incrementAndGet();
        decrementAnimalCount(animal);
    }

    private static void decrementAnimalCount(Animal animal) {
        animalPopulation.computeIfPresent(animal.getClass(), (cls, counter) -> {
            counter.decrementAndGet();
            return counter;
        });
    }

    // Регистрирует рождение животного
    public static void recordBirth(Animal animal) {
        totalBirths.incrementAndGet();
        registerAnimal(animal);
    }

    // Обновляет общее количество растений на острове
    public static void updatePlants(int total) {
        totalPlants.set(total);
    }

    // Получает количество смертей от голода за текущий день
    public static int getDeathsByHungerCount() {
        return deathsByHungerToday.get();
    }

    // Сбрасывает дневную статистику смертей от голода
    public static void resetDailyHungerDeaths() {
        deathsByHungerToday.set(0);
    }

    // Выводит общую статистику
    public static void printStatistics() {
        System.out.println("\n=== 📊 СТАТИСТИКА ОСТРОВА ===\n");

        System.out.println("Популяция животных:");
        System.out.println("--------------------");
        animalPopulation.entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getKey().getClass().getSimpleName()))
                .forEach(entry -> {
                    Animal animal = null;
                    try {
                        animal = entry.getKey().newInstance();
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    int count = entry.getValue().get();
                    String emoji = animal.getEmoji();
                    String name = animal.getClass().getSimpleName();
                    System.out.printf("%s %-10s : %3d%n", emoji, name, count);
                });

        System.out.println("\n🌿 Растений: " + totalPlants.get());

        System.out.println("\nДинамика:");
        System.out.println("---------");
        System.out.printf("Рождений           : %d%n", totalBirths.get());
        System.out.printf("Смертей от голода  : %d%n", totalDeathsByHunger.get());
        System.out.printf("Смертей от хищников: %d%n", totalDeathsByPredation.get());

        System.out.println("\n==============================\n");
    }
}
