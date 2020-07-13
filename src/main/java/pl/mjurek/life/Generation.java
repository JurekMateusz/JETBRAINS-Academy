package pl.mjurek.life;

import java.util.Arrays;
import java.util.Random;

public class Generation {
    private boolean[][] generation;

    public Generation(int size) {
        generation = new boolean[size][size];
        createFirstGeneration();
    }

    public void setCellGeneration(int i, int j, boolean val) {
        generation[i][j] = val;
    }


    public boolean[][] getCopyOfGeneration() {
        return Arrays.stream(generation)
                .map(boolean[]::clone)
                .toArray(boolean[][]::new);
    }

    private void createFirstGeneration() {
        Random random = new Random();

        for (int i = 0; i < generation.length; i++) {
            for (int j = 0; j < generation.length; j++) {
                generation[i][j] = random.nextBoolean();
            }
        }
    }


    public int getNumberOfAlives() {
        int result = 0;
        for (int i = 0; i < generation.length; i++) {
            for (int j = 0; j < generation.length; j++) {
                if (generation[i][j]) {
                    result++;
                }
            }
        }
        return result;
    }
}
