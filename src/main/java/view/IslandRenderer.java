package view;

import model.Island;
import model.Location;
import statistics.Statistics;

public class IslandRenderer {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[36m";

    private static final int CELL_WIDTH = 7; // 2 emoji + отступы

    public static void render(Island island) {
        // 🔠 Легенда перед таблицей
        System.out.println();
        System.out.println("📘 Легенда:");
        System.out.println("  " + ANSI_GREEN + "🌱" + ANSI_RESET + " — растение");
        System.out.println("  " + ANSI_YELLOW + "🐑" + ANSI_RESET + " — травоядное");
        System.out.println("  " + ANSI_RED + "🐺" + ANSI_RESET + " — хищник");
        System.out.println("  " + ANSI_CYAN + "🌊" + ANSI_RESET + " — пустая клетка");
        System.out.println();

        // 🗺️ Заголовок
        System.out.println("🗺️  Карта острова " + island.getWidth() + "x" + island.getHeight());

        // ⬜ Верхняя граница
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
                String cellContent = formatCell(cell);
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

    private static String formatCell(Location cell) {
        String first = " ";
        String second = " ";

        if (cell.getPlantCount() > 0) first = ANSI_GREEN + "🌱" + ANSI_RESET;

        if (cell.hasPredators()) {
            second = ANSI_RED + "🐺" + ANSI_RESET;
        } else if (cell.hasHerbivores()) {
            second = ANSI_YELLOW + "🐑" + ANSI_RESET;
        }

        if (first.equals(" ") && second.equals(" ")) {
            return ANSI_CYAN + "🌊 " + ANSI_RESET;
        }

        return first + second;
    }

    private static String repeat(String str, int count) {
        return str.repeat(count);
    }

    private static String padCenter(String str, int width) {
        String stripped = str.replaceAll("\u001B\\[[;\\d]*m", "");
        int visibleLength = stripped.codePointCount(0, stripped.length());
        int padSize = width - visibleLength;
        int padStart = padSize / 2;
        int padEnd = padSize - padStart;
        return " ".repeat(Math.max(0, padStart)) + str + " ".repeat(Math.max(0, padEnd));
    }
}
