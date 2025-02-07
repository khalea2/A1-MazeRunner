package ca.mcmaster.se2aa4.mazerunner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.List;

public class Explorer {
    private static final Logger logger = LogManager.getLogger();

    private Maze maze;
    private int[] currentPos;
    private int[] start;
    private int[] end;
    private boolean[][] visited;
    private List<String> moves;
    private int direction = 0; // orientation, 0: Right, 1: Down, 2: Left, 3: Up

    public Explorer(Maze mazeMap) {
        this.maze = mazeMap;
        this.visited = new boolean[mazeMap.getRows()][mazeMap.getCols()];
        this.currentPos = mazeMap.getLeftOpening();
        this.start = currentPos;
        this.end = mazeMap.getRightOpening();
        this.moves = new ArrayList<>();
        this.direction = 0;
    }

    public void exploreMaze() {
        if (currentPos == null) {
            logger.error("No valid starting point found in the maze.");
            return;
        }

        logger.info("Starting exploration at position: ({}, {})", currentPos[0], currentPos[1]);

        while (moveForward()) {
            logger.trace("Moved forward to position: ({}, {})", currentPos[0], currentPos[1]);
            if (currentPos[0] == end[0] && currentPos[1] == end[1]) {
                break;
            }
        }

        logger.info("Explorer stopped at position: ({}, {})", currentPos[0], currentPos[1]);

        logger.info("Final moves: {}", String.join("", moves));
    }

    private boolean isValidPosition(int x, int y) {
        boolean inBoundary = x >= 0 && x < maze.getCols() && y >= 0 && y < maze.getRows();
        boolean isPath = maze.getGridAt(x, y) != '#';
        return inBoundary && isPath;
    }

    private boolean moveForward() {
        int nextX = currentPos[0];
        int nextY = currentPos[1];

        if (currentPos[0] != end[0] || currentPos[1] != end[1]) {
            switch (direction) {
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
            currentPos = new int[] { nextX, nextY };
            moves.add("F");
            return true;
        }

        logger.warn("Hit a wall at position: ({}, {})", nextX, nextY);
        return false;
    }

    private void rotateRight() {
        direction = (direction + 1) % 4;
        logger.trace("Turning Right");
        moveForward();
    }

    private void rotateLeft() {
        direction = (direction + 3) % 4;
        logger.trace("Turning Left");
        moveForward();
    }

    public int[] getCurrentPosition() {
        return currentPos;
    }

    public boolean[][] getExploredCells() {
        return visited;
    }

    public List<String> getPathSteps() {
        return moves;
    }

    public void exploreRightHandRule() {
        if (currentPos == null) {
            logger.error("No valid start point foiund in maze!");
        }

        logger.info("Starting right-hand rule exploration at position: ({}, {})", currentPos[0], currentPos[1]);

        while (!reachedEnd()) {
            if (canMoveRight()) {
                turnRight();
                moveForward();
            } else if (canMoveForward()) {
                moveForward();
            } else if (canMoveLeft()) {
                turnLeft();
                moveForward();
            } else { // for dead-ends
                turnAround();
                moveForward();
            }
        }

        logger.info("Explorer stopped at position: ({}, {})", currentPos[0], currentPos[1]);
        logger.info("Final moves: {}", String.join("", moves));
    }

    private boolean reachedEnd() {
        return currentPos[0] == end[0] && currentPos[1] == end[1];
    }

    private boolean canMoveForward() {
        int newX = currentPos[0];
        int newY = currentPos[1];

        switch (direction) {
            case 0:
                newX++;
                break;
            case 1:
                newY++;
                break;
            case 2:
                newX--;
                break;
            case 3:
                newY--;
                break;
        }
        return isValidMove(newX, newY);
    }

    private boolean canMoveRight() {
        direction = (direction + 1) % 4;
        boolean canMove = canMoveForward();
        direction = (direction + 3) % 4;
        return canMove;
    }

    private boolean canMoveLeft() {
        direction = (direction + 3) % 4;
        boolean canMove = canMoveForward();
        direction = (direction + 1) % 4;
        return canMove;
    }

    private void turnRight() {
        direction = (direction + 1) % 4;
        moves.add("R");
    }

    private void turnLeft() {
        direction = (direction + 3) % 4;
        moves.add("L");
    }

    private void turnAround() {
        direction = (direction + 2) % 4;
        moves.add("L");
        moves.add("L");
    }

    private boolean isValidMove(int x, int y) {
        return isValidPosition(x, y);
    }
}
