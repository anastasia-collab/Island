package simulation;

import model.Island;
import statistics.Statistics;
import utils.SimulationSettings;
import view.IslandRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Simulation {
    private final Island island;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
    private final ExecutorService animalsExecutor = Executors.newWorkStealingPool();
    private final AtomicInteger dayCounter = new AtomicInteger(0);
    private volatile boolean isRunning = false;

    public Simulation(Island island) {
        this.island = island;
    }

    public void startSimulation() {
        if (isRunning) return;
        isRunning = true;

        scheduler.scheduleAtFixedRate(
                this::processDay,
                0,
                SimulationSettings.DAY_DURATION_MS,
                TimeUnit.MILLISECONDS
        );
    }

    private void processDay() {
        int currentDay = dayCounter.incrementAndGet();
        System.out.printf("\n=== Day %d ===%n", currentDay);

        // Рост растений и обновление общей статистики
        int totalPlants = island.getAllLocations().parallelStream()
                .mapToInt(location -> {
                    location.growPlants();
                    return location.getPlantCount();
                })
                .sum();
        Statistics.updatePlants(totalPlants);

        // Поведение животных (поедание, размножение, движение, потеря сытости)
        List<Callable<Void>> tasks = new ArrayList<>();

        island.getAllLocations().forEach(location ->
                location.getAnimals().forEach(animal ->
                        tasks.add(() -> {
                            if (animal.isAlive()) {
                                animal.eat(location);
                                animal.reproduce(location);
                                animal.move(island);
                                animal.decreaseSatiety();
                            }
                            return null;
                        })
                )
        );

        try {
            animalsExecutor.invokeAll(tasks);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Вывод смертей от голода за день
        System.out.println("Смертей от голода на сегодня: " + Statistics.getDeathsByHungerCount());

        // Сброс дневной статистики
        Statistics.resetDailyHungerDeaths();

        // Периодический вывод полной статистики и карты
        if (currentDay % SimulationSettings.STATISTICS_PRINT_INTERVAL_DAYS == 0) {
            Statistics.printStatistics();
        }
        if (currentDay % SimulationSettings.ISLAND_PRINT_INTERVAL_DAYS == 0) {
            IslandRenderer.render(island);
        }
    }

    public void stopSimulation() {
        isRunning = false;
        scheduler.shutdownNow();
        animalsExecutor.shutdownNow();
        System.out.printf("%nИтоговое количество дней: %d%n", dayCounter.get());
    }
}
