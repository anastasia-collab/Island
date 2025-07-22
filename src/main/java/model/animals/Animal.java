package model.animals;

import enums.Direction;
import model.Island;
import model.Location;
import statistics.Statistics;
import utils.Randomizer;
import utils.SimulationSettings;
import utils.SimulationSettings.AnimalConfig;

public abstract class Animal {
    protected final AnimalConfig config;
    protected Location location;
    protected double satiety;
    protected boolean isAlive = true;

    public Animal(AnimalConfig config) {
        this.config = config;
        this.satiety = config.foodNeeded * SimulationSettings.INITIAL_SATIETY_PERCENT;
    }

    public void die() {
        if (isAlive) {
            isAlive = false;
            if (location != null) {
                location.removeAnimal(this);
                if (satiety <= 0) {
                    Statistics.recordDeathByHunger(this);
                } else {
                    Statistics.recordDeathByPredation(this);
                }
            }
        }
    }

    public void decreaseSatiety() {
        satiety -= config.foodNeeded * SimulationSettings.DAILY_SATIETY_LOSS;
        if (satiety <= 0) {
            dieFromHunger();
        }
    }

    private void dieFromHunger() {
        if (isAlive) {
            isAlive = false;
            if (location != null) {
                location.removeAnimal(this);
            }
            Statistics.recordDeathByHunger(this);
        }
    }

    public void move(Island island) {
        if (!isAlive || location == null || Randomizer.nextDouble() > 0.7) return;

        Direction direction = Randomizer.randomEnum(Direction.class);
        int steps = Randomizer.nextInt(1, config.maxSpeed + 1);

        int newX = Math.max(0, Math.min(location.x + direction.getDx() * steps, island.getWidth() - 1));
        int newY = Math.max(0, Math.min(location.y + direction.getDy() * steps, island.getHeight() - 1));

        Location newLocation = island.getLocation(newX, newY);
        if (newLocation != null && newLocation != location) {
            location.removeAnimal(this);
            newLocation.addAnimal(this);
            location = newLocation;
        }
    }

    public abstract void eat(Location location);
    public abstract void reproduce(Location location);
    public abstract String getEmoji();
    public abstract boolean isPredator();

    public boolean isAlive() {
        return isAlive;
    }

    public double getWeight() {
        return config.weight;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return String.format("%s(%.1f)", getEmoji(), satiety);
    }
}
