package model.animals.predators;

import model.Location;
import model.animals.Animal;
import model.animals.Predator;
import model.animals.herbivores.*;
import utils.SimulationSettings;

import java.util.List;
import java.util.stream.Collectors;

public class Wolf extends Predator {

    public Wolf() {
        super(SimulationSettings.AnimalConfig.WOLF); // Инициализация параметров:
        // - weight: 50 кг (пример)
        // - maxSpeed: 3 клетки/ход
        // - foodNeeded: 8 кг/день
        // - maxPopulation: 30 в локации
    }

    @Override
    protected List<Animal> getPotentialFood(Location location) {
        return location.getAnimals().stream()
                // Фильтр по конкретным классам-жертвам (все травоядные)
                .filter(a -> a instanceof Rabbit || a instanceof Mouse
                        || a instanceof Goat || a instanceof Sheep
                        || a instanceof Horse || a instanceof Deer
                        || a instanceof Boar || a instanceof Buffalo
                        || a instanceof Duck)
                // Исключаем уже мертвых животных
                .filter(Animal::isAlive)
                .collect(Collectors.toList());
    }

    @Override
    protected int getEatingProbability(Animal prey) {
        // Логика выбора вероятности по типу жертвы:
        if (prey instanceof Rabbit) return SimulationSettings.EatingChance.WOLF_RABBIT;    // 60%
        if (prey instanceof Mouse) return SimulationSettings.EatingChance.WOLF_MOUSE;     // 80%
        if (prey instanceof Goat) return SimulationSettings.EatingChance.WOLF_GOAT;       // 40%
        if (prey instanceof Sheep) return SimulationSettings.EatingChance.WOLF_SHEEP;     // 70%
        if (prey instanceof Horse) return SimulationSettings.EatingChance.WOLF_HORSE;     // 10%
        if (prey instanceof Deer) return SimulationSettings.EatingChance.WOLF_DEER;       // 15%
        if (prey instanceof Boar) return SimulationSettings.EatingChance.WOLF_BOAR;       // 15%
        if (prey instanceof Buffalo) return SimulationSettings.EatingChance.WOLF_BUFFALO; // 20%
        if (prey instanceof Duck) return SimulationSettings.EatingChance.WOLF_DUCK;       // 40%
        return 0; // Для неучтенных типов
    }

    @Override
    protected Animal createOffspring() {
        return new Wolf(); // Создание без параметров, так как настройки берутся из конфига
    }

    @Override
    public String getEmoji() {
        return "🐺"; // Unicode: U+1F43A
    }

    @Override
    public boolean isPredator() {
        return true;
    }
}