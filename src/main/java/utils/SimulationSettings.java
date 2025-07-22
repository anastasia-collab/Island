package utils;

import model.animals.Animal;
import model.animals.herbivores.*;
import model.animals.predators.*;

import java.util.function.Supplier;

public final class SimulationSettings {
    private SimulationSettings() {}

    // ===== Общие настройки симуляции =====
    public static final int ISLAND_WIDTH = 10;
    public static final int ISLAND_HEIGHT = 10;
    public static final int DAY_DURATION_MS = 1000;
    public static final int STATISTICS_PRINT_INTERVAL_DAYS = 5;
    public static final int ISLAND_PRINT_INTERVAL_DAYS = 2;
    public static final int SIMULATION_DURATION_MINUTES = 10;
    public static final double INITIAL_SATIETY_PERCENT = 0.7;
    public static final double DAILY_SATIETY_LOSS = 0.3;

    // ===== Настройки растений =====
    public static final int MAX_PLANTS_PER_CELL = 200;
    public static final int PLANT_GROWTH_PER_DAY = 10;

    // ===== Конфигурация животных =====
    public enum AnimalConfig {
        WOLF(50, 30, 3, 8, 0.3, 30, Wolf::new),
        BOA(15, 30, 1, 3, 0.2, 30, Boa::new),
        FOX(8, 30, 2, 2, 0.4, 30, Fox::new),
        BEAR(500, 5, 2, 80, 0.25, 5, Bear::new),
        EAGLE(6, 20, 3, 1, 0.35, 20, Eagle::new),

        HORSE(400, 20, 4, 60, 0.2, 20, Horse::new),
        DEER(300, 20, 4, 50, 0.3, 20, Deer::new),
        RABBIT(2, 150, 2, 0.45, 0.5, 150, Rabbit::new),
        MOUSE(0.05, 100, 1, 0.01, 0.6, 250, Mouse::new),
        GOAT(60, 140, 3, 10, 0.4, 140, Goat::new),
        SHEEP(70, 140, 3, 15, 0.4, 140, Sheep::new),
        BOAR(400, 50, 2, 50, 0.35, 50, Boar::new),
        BUFFALO(700, 10, 3, 100, 0.15, 200, Buffalo::new),
        DUCK(1, 200, 4, 0.15, 0.45, 1000, Duck::new),
        CATERPILLAR(0.01, 100, 0, 0, 0.7, 200, Caterpillar::new);

        public final double weight;
        public final int maxPerCell;
        public final int maxSpeed;
        public final double foodNeeded;
        public final double reproductionChance;
        public final int initialCount;
        public final Supplier<Animal> factory;

        AnimalConfig(double weight, int maxPerCell, int maxSpeed,
                     double foodNeeded, double reproductionChance,
                     int initialCount, Supplier<Animal> factory) {
            this.weight = weight;
            this.maxPerCell = maxPerCell;
            this.maxSpeed = maxSpeed;
            this.foodNeeded = foodNeeded;
            this.reproductionChance = reproductionChance;
            this.initialCount = initialCount;
            this.factory = factory;
        }

        public Animal createAnimal() {
            return factory.get();
        }

        public int getInitialCount() {
            return initialCount;
        }
    }

    // ===== Вероятности поедания (в процентах) =====
    public static final class EatingChance {
        public static final int WOLF_RABBIT = 60;
        public static final int WOLF_MOUSE = 80;
        public static final int WOLF_GOAT = 60;
        public static final int WOLF_SHEEP = 70;
        public static final int WOLF_HORSE = 10;
        public static final int WOLF_DEER = 15;
        public static final int WOLF_BOAR = 15;
        public static final int WOLF_BUFFALO = 10;
        public static final int WOLF_DUCK = 40;

        public static final int BOA_FOX = 15;
        public static final int BOA_RABBIT = 20;
        public static final int BOA_MOUSE = 40;
        public static final int BOA_DUCK = 10;

        public static final int FOX_RABBIT = 70;
        public static final int FOX_MOUSE = 90;
        public static final int FOX_DUCK = 60;
        public static final int FOX_CATERPILLAR = 40;

        public static final int BEAR_BOA = 80;
        public static final int BEAR_HORSE = 40;
        public static final int BEAR_DEER = 80;
        public static final int BEAR_RABBIT = 80;
        public static final int BEAR_MOUSE = 90;
        public static final int BEAR_GOAT = 70;
        public static final int BEAR_SHEEP = 70;
        public static final int BEAR_BOAR = 50;
        public static final int BEAR_DUCK = 10;

        public static final int EAGLE_FOX = 10;
        public static final int EAGLE_RABBIT = 90;
        public static final int EAGLE_MOUSE = 90;
        public static final int EAGLE_DUCK = 80;

        public static final int MOUSE_CATERPILLAR = 90;
        public static final int BOAR_CATERPILLAR = 90;
        public static final int DUCK_CATERPILLAR = 90;
    }
}
