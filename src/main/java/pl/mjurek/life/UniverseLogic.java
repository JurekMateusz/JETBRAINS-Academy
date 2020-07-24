package pl.mjurek.life;

public class UniverseLogic {
    private int sizeUniverse;
    private Generation generation;
    private CellStatus[][] current;

    public UniverseLogic(int sizeUniverse) {
        this.sizeUniverse = sizeUniverse;
        generation = new Generation(sizeUniverse);
    }

    public CellStatus[][] nextGeneration() {
        current = generation.getCopyOfGeneration();

        for (int i = 0; i < sizeUniverse; i++) {
            for (int j = 0; j < sizeUniverse; j++) {

                int neighbors = countCellNeighbors(i, j);
                CellStatus callVar = getCellValue(i, j);

                if (isCellReborn(neighbors, callVar) || isCellSurvive(neighbors, callVar)) {
                    generation.setCellGeneration(i, j, CellStatus.ALIVE);
                } else {
                    generation.setCellGeneration(i, j, CellStatus.DEAD);
                }
            }
        }
        return generation.getCopyOfGeneration();
    }

    public int countCellNeighbors(int i, int j) {
        int sum = 0;
        sum += countGrid(i - 1, j);        //W
        sum += countGrid(i - 1, j - 1); //NW
        sum += countGrid(i, j - 1);       //N
        sum += countGrid(i + 1, j - 1); //NE
        sum += countGrid(i + 1, j);       //E
        sum += countGrid(i + 1, j + 1); //SE
        sum += countGrid(i, j + 1);        //S
        sum += countGrid(i - 1, j + 1); //SW
        return sum;
    }

    private int countGrid(int i, int j) {
        int sizeMap = current.length;
        int ii = (i % sizeMap + sizeMap) % sizeMap;
        int jj = (j % sizeMap + sizeMap) % sizeMap;

        return current[ii][jj] == CellStatus.ALIVE ? 1 : 0;
    }

    private CellStatus getCellValue(int i, int j) {
        return current[i][j];
    }

    private boolean isCellSurvive(int number, CellStatus cellVal) {
        return (number == 2 || number == 3) && cellVal == CellStatus.ALIVE;
    }

    private boolean isCellReborn(int number, CellStatus cellVal) {
        return number == 3 && cellVal == CellStatus.DEAD;
    }

    public Generation getGeneration() {
        return generation;
    }
}