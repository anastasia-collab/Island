package model;

import statistics.Statistics;
import utils.SimulationSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс Island представляет собой остров, на котором расположены локации и животные
 */
public class Island {
    private final int width;
    private final int height;
    private final Location[][] grid;

    public Island() {
        this.width = SimulationSettings.ISLAND_WIDTH;
        this.height = SimulationSettings.ISLAND_HEIGHT;
        this.grid = new Location[width][height];

        initializeLocations();
        spawnInitialAnimals();
    }

    private void initializeLocations() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Location(x, y);
            }
        }
    }

    private void spawnInitialAnimals() {
        for (SimulationSettings.AnimalConfig config : SimulationSettings.AnimalConfig.values()) {
            for (int i = 0; i < config.getInitialCount(); i++) {
                Location location = getRandomLocation();
                var animal = config.createAnimal();
                location.addAnimal(animal);
                Statistics.registerAnimal(animal); // <--- добавлен учёт в статистике
            }
        }
    }

    public Location getRandomLocation() {
        int x = (int) (Math.random() * width);
        int y = (int) (Math.random() * height);
        return grid[x][y];
    }

    public Location getLocation(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return grid[x][y];
        }
        return null;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Location[][] getGrid() {
        return grid;
    }

    /**
     * Возвращает все локации острова в виде плоского списка
     */
    public List<Location> getAllLocations() {
        List<Location> allLocations = new ArrayList<>(width * height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                allLocations.add(grid[x][y]);
            }
        }
        return allLocations;
    }
}
