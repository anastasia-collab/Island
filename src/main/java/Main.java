import model.Island;
import simulation.Simulation;
import utils.SimulationSettings;
import view.IslandRenderer;

// Главный класс программы, содержащий точку входа
public class Main {
    public static void main(String[] args) {
        // 1. Инициализация основных компонентов
        Island island = new Island(); // Создание модели острова
        Simulation simulation = new Simulation(island); // Создание движка симуляции

        // 2. Запуск симуляции
        simulation.startSimulation(); // Старт потока выполнения симуляции

        // 3. Тайминг выполнения программы
        try {
            // Перевод минут в миллисекунды:
            // MINUTES * 60 секунд * 1000 миллисекунд
            Thread.sleep(
                    SimulationSettings.SIMULATION_DURATION_MINUTES * 60 * 1000L
            );
        } catch (InterruptedException e) {
            // Обработка прерывания сна потока
            Thread.currentThread().interrupt(); // Восстановление флага прерывания
            System.err.println("Симуляция была прервана досрочно!");
        }

        // 4. Завершающие действия
        simulation.stopSimulation(); // Остановка потока симуляции

        // 5. Визуализация результатов
        System.out.println("\n=== Финальное состояние острова ===");
        IslandRenderer.render(island); // Вывод ASCII-представления острова
    }
}