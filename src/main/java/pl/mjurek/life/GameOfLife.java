package pl.mjurek.life;

import javax.swing.*;
import java.awt.*;

public class GameOfLife extends JFrame {
    private JPanel statsPanel;
    private JLabel aliveLabel;
    private JLabel generationLabel;
    private LifePanel lifeBoardPanel;

    public GameOfLife() {
        super("Game of Life");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setLocationRelativeTo(null);

        initComponents();

        //this should come last
        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        statsPanel = new JPanel();
        statsPanel.setName("Stats Panel");

        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        add(statsPanel, BorderLayout.PAGE_START);

        generationLabel = new JLabel();
        generationLabel.setName("GenerationLabel");
        generationLabel.setText("Generation #0");
        statsPanel.add(generationLabel);

        aliveLabel = new JLabel();
        aliveLabel.setName("AliveLabel");
        aliveLabel.setText("Alive: 0");
        statsPanel.add(aliveLabel);

        lifeBoardPanel = new LifePanel();
        lifeBoardPanel.setName("Life Board Panel");
        add(lifeBoardPanel, BorderLayout.CENTER);
    }

    public void play() {
        UniverseLogic universe = new UniverseLogic(30);

        int currentGen = 1;
        while (true) {
            sleep();
            boolean[][] nextGen = universe.nextGeneration();
            int alive = universe.getGeneration().getNumberOfAlives();

            updateVisuals(currentGen, alive, nextGen);

            if (universe.isLifeFinalForm(nextGen)) break;
            currentGen++;
        }
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateVisuals(int currentGen, int alive, boolean[][] nextGen) {
        generationLabel.setText("Generation #" + currentGen);
        generationLabel.repaint();

        aliveLabel.setText("Alive: " + alive);
        aliveLabel.repaint();

        lifeBoardPanel.updateCellArray(nextGen);
        lifeBoardPanel.repaint();
    }
}