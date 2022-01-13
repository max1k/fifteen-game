package ru.mxk.game.fifteen.component.gameplay;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GameplayService {
    private static final int SIZE = 5;
    private static final int EMPTY_CELL_VALUE = SIZE * SIZE;

    private final int[] cells = new int[SIZE * SIZE];
    private final Map<Integer, Integer> valueIndexMap = new HashMap<>(SIZE * SIZE);


    public GameplayService() {
        for(int i = 0; i < SIZE * SIZE; i++) {
            final int cellValue = i + 1;
            cells[i] = cellValue;
            valueIndexMap.put(cellValue, i);
        }
    }

    public int getSize() {
        return SIZE;
    }

    public Optional<Integer> getIndexByValue(final int value) {
        return Optional.ofNullable(valueIndexMap.get(value));
    }

    public boolean isEmpty(final int index) {
        return cells[index] == EMPTY_CELL_VALUE;
    }

    public boolean trySwap(int cellValue) {
        final int cellIndex = getIndexByValue(cellValue).orElseThrow(() -> new IndexOutOfBoundsException(cellValue));

        final Optional<Integer> emptyNeighborIndex =
                Stream.of(cellIndex - 1, cellIndex + 1, cellIndex - SIZE, cellIndex + SIZE)
                      .filter(index -> index >= 0 && index < SIZE * SIZE)
                      .filter(this::isEmpty)
                      .findFirst();

        if (emptyNeighborIndex.isPresent()) {
            swap(cellIndex, emptyNeighborIndex.get());
            return true;
        }

        return false;
    }

    private void swap(int firstIndex, int secondIndex) {
        final int temp = cells[firstIndex];
        cells[firstIndex] = cells[secondIndex];
        cells[secondIndex] = temp;

        valueIndexMap.put(cells[firstIndex], firstIndex);
        valueIndexMap.put(cells[secondIndex], secondIndex);
    }

    public boolean checkWin() {
        int number = 1;
        for (Integer cell : cells) {
            if (cell != number++) {
                return false;
            }
        }

        return true;
    }

    public <T> List<T> mapCells(IntFunction<T> mapper) {
        return Arrays.stream(cells)
                     .mapToObj(mapper)
                     .collect(Collectors.toList());
    }
}
