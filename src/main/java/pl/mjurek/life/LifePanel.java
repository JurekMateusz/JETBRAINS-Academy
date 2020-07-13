package pl.mjurek.life;

import javax.swing.*;
import java.awt.*;

public class LifePanel extends JPanel {
    private GameCell[][] cellArray;

    public LifePanel() {
        super();
        setBorder(BorderFactory.createLineBorder(Color.black));
        setBounds(1, 41, 401, 401);
    }

    public void initialize(int dimension) {
        this.setLayout(new GridLayout(dimension, dimension, 1, 1));
        this.cellArray = new GameCell[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                cellArray[i][j] = new GameCell();
                this.add(cellArray[i][j]);
            }
        }
    }

    public void updateCellArray(boolean[][] board) {

        if (cellArray != null) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    cellArray[i][j].setAlive(board[i][j]);
                }
            }
            repaint();
        } else {
            initialize(board.length);
            updateCellArray(board);
        }
    }

    private class GameCell extends JPanel {
        private boolean alive;

        public GameCell() {
            super();
            setVisible(false);
            setBackground(Color.BLACK);
        }

        public void setAlive(boolean alive) {
            this.alive = alive;
            setVisible(this.alive);
        }
    }
}