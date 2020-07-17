package pl.mjurek.life;

import java.util.Arrays;
import java.util.Random;

public class Generation {
    private final CellStatus[][] generation;

    public Generation(int size) {
        generation = new CellStatus[size][size];
        createFirstGeneration();
    }

    public void setCellGeneration(int i, int j, CellStatus val) {
        generation[i][j] = val;
    }

    public CellStatus[][] getCopyOfGeneration() {
        return Arrays.stream(generation)
                .map(CellStatus[]::clone)
                .toArray(CellStatus[][]::new);
    }

    private void createFirstGeneration() {
        Random random = new Random();
        for (int i = 0; i < generation.length; i++) {
            for (int j = 0; j < generation.length; j++) {
                boolean res = random.nextBoolean();
                generation[i][j] = res ? CellStatus.ALIVE : CellStatus.DEAD;
            }
        }
    }

    public int getNumberOfAlives() {
        return (int) Arrays.stream(generation)
                .flatMap(Arrays::stream)
                .filter(i -> i == CellStatus.ALIVE)
                .count();
    }
}