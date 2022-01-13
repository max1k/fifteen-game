package ru.mxk.game.fifteen.component.gameplay;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mxk.game.fifteen.configuration.GameOptions;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class GameplayService {
    private final GameOptions options;

    private int[] cells;
    private Map<Integer, Integer> valueIndexMap;

    @PostConstruct
    private void init() {
        cells = new int[getCellsCount()];
        valueIndexMap = new HashMap<>(getCellsCount());

        reset();
        shuffle();
    }

    public int getSize() {
        return options.getSize();
    }

    private int getCellsCount() {
        return options.getSize() * options.getSize();
    }

    public void reset() {
        for(int i = 0; i < getCellsCount(); i++) {
            final int cellValue = i + 1;
            cells[i] = cellValue;
            valueIndexMap.put(cellValue, i);
        }
    }

    public void shuffle() {
        for(int i = 0; i < getSize() * getSize() * getSize(); i++) {
            final int emptyCellIdx = valueIndexMap.get(getCellsCount());
            final List<Integer> neighborCells =
                    Stream.of(emptyCellIdx - 1, emptyCellIdx + 1, emptyCellIdx - getSize(), emptyCellIdx + getSize())
                          .filter(index -> index >= 0 && index < getCellsCount())
                          .map(index -> cells[index])
                          .collect(Collectors.toList());
            final int randomNeighborValue =  neighborCells.get(new Random().nextInt(neighborCells.size()));
            trySwap(randomNeighborValue);
        }
    }

    public Optional<Integer> getIndexByValue(final int value) {
        return Optional.ofNullable(valueIndexMap.get(value));
    }

    public boolean isEmpty(final int index) {
        return cells[index] == getCellsCount();
    }

    public boolean trySwap(int cellValue) {
        final int cellIndex = getIndexByValue(cellValue).orElseThrow(() -> new IndexOutOfBoundsException(cellValue));

        final Optional<Integer> emptyNeighborIndex =
                Stream.of(cellIndex - 1, cellIndex + 1, cellIndex - getSize(), cellIndex + getSize())
                      .filter(index -> index >= 0 && index < getCellsCount())
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
