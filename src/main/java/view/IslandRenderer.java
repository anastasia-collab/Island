package view;

import model.Island;
import model.Location;
import statistics.Statistics;

public class IslandRenderer {
    private static final int CELL_WIDTH = 5;

    public static void render(Island island) {
        // 🔠 Легенда
        System.out.println();
        System.out.println("📘 Легенда:");
        System.out.println("  * — растение");
        System.out.println("  h — травоядное");
        System.out.println("  P — хищник");
        System.out.println("  . — пустая клетка");
        System.out.println();

        // 🗺️ Заголовок
        System.out.println("🗺️  Карта острова " + island.getWidth() + "x" + island.getHeight());

        // Границы
        String horizontalBorder = "╔" + repeat("═", CELL_WIDTH);
        for (int i = 1; i < island.getWidth(); i++) {
            horizontalBorder += "╦" + repeat("═", CELL_WIDTH);
        }
        horizontalBorder += "╗";

        String rowSeparator = "╠" + repeat("═", CELL_WIDTH);
        for (int i = 1; i < island.getWidth(); i++) {
            rowSeparator += "╬" + repeat("═", CELL_WIDTH);
        }
        rowSeparator += "╣";

        String bottomBorder = "╚" + repeat("═", CELL_WIDTH);
        for (int i = 1; i < island.getWidth(); i++) {
            bottomBorder += "╩" + repeat("═", CELL_WIDTH);
        }
        bottomBorder += "╝";

        System.out.println(horizontalBorder);

        for (int y = 0; y < island.getHeight(); y++) {
            StringBuilder row = new StringBuilder("║");

            for (int x = 0; x < island.getWidth(); x++) {
                Location cell = island.getLocation(x, y);
                String cellContent = formatAsciiCell(cell);
                row.append(padCenter(cellContent, CELL_WIDTH)).append("║");
            }

            System.out.println(row);
            if (y < island.getHeight() - 1) {
                System.out.println(rowSeparator);
            }
        }

        System.out.println(bottomBorder);

        System.out.println(); // Пустая строка перед статистикой
        Statistics.printStatistics(); // Статистика
    }

    private static String formatAsciiCell(Location cell) {
        String first = " ";
        String second = " ";

        if (cell.getPlantCount() > 0) first = "*";

        if (cell.hasPredators()) {
            second = "P";
        } else if (cell.hasHerbivores()) {
            second = "h";
        }

        if (first.equals(" ") && second.equals(" ")) {
            return ". ";
        }

        return first + second;
    }

    private static String repeat(String str, int count) {
        return str.repeat(count);
    }

    private static String padCenter(String str, int width) {
        int visibleLength = str.length();
        int padSize = width - visibleLength;
        int padStart = padSize / 2;
        int padEnd = padSize - padStart;
        return " ".repeat(Math.max(0, padStart)) + str + " ".repeat(Math.max(0, padEnd));
    }
}
