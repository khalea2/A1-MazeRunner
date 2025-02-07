package ca.mcmaster.se2aa4.mazerunner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.List;

public class Explorer {
    private static final Logger logger = LogManager.getLogger();

    private Maze mazeMap;
    private int[] currentLocation;
    private int[] startPoint;
    private int[] endPoint;
    private boolean[][] exploredCells;
    private List<String> pathSteps;
    private int orientation = 0; // orientation, 0: Right, 1: Down, 2: Left, 3: Up

    public Explorer(Maze mazeMap) {
        this.mazeMap = mazeMap;
        this.exploredCells = new boolean[mazeMap.getRows()][mazeMap.getCols()];
        this.currentLocation = mazeMap.getLeftOpening();
        this.startPoint = currentLocation;
        this.endPoint = mazeMap.getRightOpening();
        this.pathSteps = new ArrayList<>();
        this.orientation = 0;
    }

    public void exploreMaze() {
        if (currentLocation == null) {
            logger.error("No valid starting point found in the maze.");
            return;
        }

        logger.info("Starting exploration at position: ({}, {})", currentLocation[0], currentLocation[1]);

        while (stepForward()) {
            pathSteps.add("F");
            logger.trace("Moved forward to position: ({}, {})", currentLocation[0], currentLocation[1]);
            if (currentLocation[0] == endPoint[0] && currentLocation[1] == endPoint[1]) {
                break;
            }
        }

        logger.info("Explorer stopped at position: ({}, {})", currentLocation[0], currentLocation[1]);

        logger.info("Final moves: {}", String.join("", pathSteps));
    }

    private boolean isValidPosition(int x, int y) {
        boolean inBoundary = x >= 0 && x < mazeMap.getCols() && y >= 0 && y < mazeMap.getRows();
        boolean isPath = mazeMap.getGridAt(x, y) != '#';
        return inBoundary && isPath;
    }

    private boolean stepForward() {
        int nextX = currentLocation[0];
        int nextY = currentLocation[1];

        if (currentLocation[0] != endPoint[0] || currentLocation[1] != endPoint[1]) {
            switch (orientation) {
                case 0:
                    nextX++;
                    break; // Move right
                case 1:
                    nextY++;
                    break; // Move down
                case 2:
                    nextX--;
                    break; // Move left
                case 3:
                    nextY--;
                    break; // Move up
            }
        } else {
            logger.trace("Reached end!");
            return false;
        }

        if (isValidPosition(nextX, nextY)) {
            currentLocation = new int[] { nextX, nextY };
            return true;
        }

        logger.warn("Hit a wall at position: ({}, {})", nextX, nextY);
        return false;
    }

    private void rotateRight() {
        orientation = (orientation + 1) % 4;
        logger.trace("Turning Right");
        stepForward();
    }

    private void rotateLeft() {
        orientation = (orientation + 3) % 4;
        logger.trace("Turning Left");
        stepForward();
    }

    public int[] getCurrentPosition() {
        return currentLocation;
    }

    public boolean[][] getExploredCells() {
        return exploredCells;
    }

    public List<String> getPathSteps() {
        return pathSteps;
    }
}
