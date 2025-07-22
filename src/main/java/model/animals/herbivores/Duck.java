package model.animals.herbivores;

import model.animals.Animal;
import model.animals.CaterpillarEater;
import model.animals.Herbivore;
import utils.SimulationSettings;

public class Duck extends Herbivore implements CaterpillarEater {

    public Duck() {
        // Вызов родительского конструктора с конфигурацией для утки
        super(SimulationSettings.AnimalConfig.DUCK);
    }

    @Override
    public int getCaterpillarEatingChance() {
        return SimulationSettings.EatingChance.DUCK_CATERPILLAR;
    }

    @Override
    protected Animal createOffspring() {
        // Создаем новую утку с теми же параметрами
        return new Duck();
    }

    @Override
    public String getEmoji() {
        return "🦆";
    }

    @Override
    public boolean isPredator() {
        return false;
    }
}
