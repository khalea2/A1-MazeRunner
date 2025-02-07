package ca.mcmaster.se2aa4.mazerunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// represents the maze structure and handles maze file loading
public class Maze {
    // maze structure as a 2D char array
    private char[][] grid;
    private int rows;
    private int cols;

    // coordinates for maze entrance and exit
    private int[] leftOpening;
    private int[] rightOpening;

    private static final Logger logger = LogManager.getLogger();

    public Maze(String filePath) {
        try {
            loadMaze(filePath);
            getOpenings();
        } catch (IOException e) {
            logger.error("Failed to load the maze from file: {}. Error: {}", filePath, e.getMessage());
        }
    }

    // loads and validates maze from file
    private void loadMaze(String filePath) throws IOException {
        logger.info("Loading Maze");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            rows = 0;
            // first pass: get the grid dimensions
            while ((line = reader.readLine()) != null) {
                cols = line.length(); // use first line to determine width
                rows++; // count total lines for height
            }
        } catch (IOException e) {
            logger.error("Error determining the grid dimensions from file: {}. Error: {}", filePath, e.getMessage());
            throw e;
        }
        reader.close();

        // second pass: populate the grid
        try {
            grid = new char[rows][cols];
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                grid[row] = line.toCharArray(); // convert each line to char array for maze representation
                row++;

                StringBuilder output = new StringBuilder();
                for (int idx = 0; idx < line.length(); idx++) {
                    if (line.charAt(idx) == '#') {
                        output.append("WALL "); // mark walls for logging
                    } else if (line.charAt(idx) != '#') {
                        output.append("PASS "); // mark passages for logging
                    }
                }
                logger.trace(output.toString());
            }
        } catch (IOException e) {
            logger.error("Error populating the maze grid from file: {}. Error: {}", filePath, e.getMessage());
            throw e;
        }
        reader.close();
    }

    // identifies entrance and exit points on maze borders
    public void getOpenings() {
        for (int row = 0; row < rows; row++) {
            if (grid[row][0] == ' ') {
                this.leftOpening = new int[] { 0, row };
            }
            if (grid[row][cols - 1] != '#') {
                this.rightOpening = new int[] { (cols - 1), row };
            }
        }

        if (this.leftOpening == null) {
            logger.error("Error reading start point");
        }
        if (this.rightOpening == null) {
            logger.error("Error reading end point");
        }
        logger.info("Maze start ({}, {}) end ({}, {}) points read successfully", leftOpening[0], leftOpening[1],
                rightOpening[0], rightOpening[1]);
    }

    public char getGridAt(int x, int y) {
        return grid[y][x];
    }

    public int[] getLeftOpening() {
        return leftOpening;
    }

    public int[] getRightOpening() {
        return rightOpening;
    }

    public char[][] getGrid() {
        return grid;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}
