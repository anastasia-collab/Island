package enums;

public enum Direction {
    UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

    // Фиксированные изменения координат для направления
    private final int dx; // Дельта по оси X
    private final int dy; // Дельта по оси Y

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }
}