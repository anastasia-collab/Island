package model;

import model.animals.Animal;
import statistics.Statistics;
import utils.SimulationSettings;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Location {
    public final int x, y;

    private final ConcurrentHashMap<Class<? extends Animal>, BlockingQueue<Animal>> animals = new ConcurrentHashMap<>();
    private final BlockingQueue<Plant> plants = new LinkedBlockingQueue<>(SimulationSettings.MAX_PLANTS_PER_CELL);

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
        initializePlants();
    }

    private void initializePlants() {
        int initialPlants = SimulationSettings.PLANT_GROWTH_PER_DAY / 2;
        for (int i = 0; i < initialPlants; i++) {
            plants.offer(new Plant());
        }
    }

    public void addAnimal(Animal animal) {
        Class<? extends Animal> type = animal.getClass();
        int max = SimulationSettings.AnimalConfig.valueOf(type.getSimpleName().toUpperCase()).maxPerCell;

        BlockingQueue<Animal> queue = animals.computeIfAbsent(type, k -> new LinkedBlockingQueue<>(max));
        if (queue.offer(animal)) {
            Statistics.recordBirth(animal);
        }
    }

    public void removeAnimal(Animal animal) {
        Class<? extends Animal> type = animal.getClass();
        animals.computeIfPresent(type, (k, queue) -> {
            queue.remove(animal);
            return queue.isEmpty() ? null : queue;
        });
    }

    public List<Animal> getAnimals() {
        return animals.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public <T extends Animal> List<T> getAnimalsByType(Class<T> type) {
        BlockingQueue<Animal> queue = animals.get(type);
        if (queue == null) return Collections.emptyList();
        return queue.stream().map(type::cast).collect(Collectors.toList());
    }

    public double eatPlants(double amount) {
        int maxToEat = Math.min((int) (amount / Plant.WEIGHT), plants.size());
        for (int i = 0; i < maxToEat; i++) {
            plants.poll();
        }
        return maxToEat * Plant.WEIGHT;
    }

    public void growPlants() {
        int spaceLeft = SimulationSettings.MAX_PLANTS_PER_CELL - plants.size();
        int newPlants = Math.min(SimulationSettings.PLANT_GROWTH_PER_DAY, Math.max(0, spaceLeft));
        for (int i = 0; i < newPlants; i++) {
            plants.offer(new Plant());
        }
    }

    public String getDominantAnimalInfo() {
        if (animals.isEmpty()) return "";

        return animals.entrySet().stream()
                .filter(e -> !e.getValue().isEmpty())
                .map(e -> Map.entry(e.getKey(), (long) e.getValue().size()))
                .max(Map.Entry.comparingByValue())
                .map(entry -> {
                    try {
                        String emoji = entry.getKey().getDeclaredConstructor().newInstance().getEmoji();
                        return entry.getValue() + emoji;
                    } catch (Exception e) {
                        return entry.getValue() + "?";
                    }
                })
                .orElse("");
    }

    public int getPlantCount() {
        return plants.size();
    }

    // ---- Новые методы ----

    /**
     * Проверяет, есть ли на локации хотя бы один хищник
     */
    public boolean hasPredators() {
        return animals.values().stream()
                .flatMap(Collection::stream)
                .anyMatch(Animal::isPredator);
    }

    /**
     * Проверяет, есть ли на локации хотя бы один травоядный
     */
    public boolean hasHerbivores() {
        return animals.values().stream()
                .flatMap(Collection::stream)
                .anyMatch(animal -> !animal.isPredator());
    }

    @Override
    public String toString() {
        String animalsInfo = getDominantAnimalInfo();
        String plantsInfo = getPlantCount() > 0 ? " " + getPlantCount() + "🌱" : "";
        return (animalsInfo.isEmpty() && plantsInfo.isEmpty()) ? "🌊" : animalsInfo + plantsInfo;
    }
}
