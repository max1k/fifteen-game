package ru.mxk.game.fifteen.component.gameplay;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
class GameplayServiceTest {
    @Autowired
    private GameplayService gameplayService;

    private int getMaxValue() {
        return gameplayService.getSize() * gameplayService.getSize();
    }

    private int getLastIndex() {
        return gameplayService.getSize() * gameplayService.getSize() - 1;
    }

    @BeforeEach
    void reset() {
        gameplayService.reset();
    }

    @Test
    void testGetIndex() {
        final int indexByLastCellValue = gameplayService.getIndexByValue(getMaxValue()).orElse(-1) + 1;
        Assertions.assertEquals(getMaxValue(), indexByLastCellValue);
        Assertions.assertEquals(0, gameplayService.getIndexByValue(1).orElse(-1));
    }


    @Test
    void testIsEmpty() {
        Assertions.assertTrue(gameplayService.isEmpty(getLastIndex()));
        Assertions.assertFalse(gameplayService.isEmpty(0));
    }

    @Test
    void testTrySwap() {
        gameplayService.trySwap(getMaxValue() - gameplayService.getSize());
        Assertions.assertTrue(gameplayService.isEmpty(getLastIndex() - gameplayService.getSize()));
        Assertions.assertFalse(gameplayService.isEmpty(getLastIndex()));
    }

    @Test
    void testCheckWin() {
        Assertions.assertTrue(gameplayService.checkWin());
        gameplayService.trySwap(getMaxValue() - gameplayService.getSize());
        Assertions.assertFalse(gameplayService.checkWin());
    }

    @Test
    void testMapCells() {
        final List<Integer> expectedValues = IntStream.range(1, getMaxValue() + 1)
                                                      .boxed()
                                                      .collect(Collectors.toList());
        final List<Integer> actualValues = gameplayService.mapCells(it -> it);
        Assertions.assertEquals(expectedValues, actualValues);
    }
}