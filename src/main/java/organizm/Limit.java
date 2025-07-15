package organizm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

//Воспользуемся аннотациями Lombok для меньшего количества кода
@Getter
@RequiredArgsConstructor
public class Limit {
    private final int maxCountInCell; //максимально возможное число животных в клетке
    private final double maxWeight; //максимальный вес животного
    private final int maxSpeed; //максимальная скорость передвижения
    private final int maxFood; //максимальное количество  еды
    private final int flockSize; //размер стаи
}
