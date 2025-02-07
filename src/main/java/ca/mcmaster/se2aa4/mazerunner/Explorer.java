package ca.mcmaster.se2aa4.mazerunner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.List;

// handles maze traversal and path finding logic
public class Explorer { // Explorer class for exploring the maze
    private static final Logger logger = LogManager.getLogger();

    private Maze maze;
    private int[] currentPos;
    private int[] start;
    private int[] end;
    private boolean[][] visited;
    private List<String> moves;
    // direction represents current facing: 0=right, 1=down, 2=left, 3=up
    private int direction = 0;

    public Explorer(Maze mazeMap) { // constructor for the Explorer class
        this.maze = mazeMap;
        this.visited = new boolean[mazeMap.getRows()][mazeMap.getCols()];
        this.currentPos = mazeMap.getLeftOpening();
        this.start = currentPos;
        this.end = mazeMap.getRightOpening();
        this.moves = new ArrayList<>();
        this.direction = 0;
    }

    public void exploreMaze() { // explore the maze basic method
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

    // checks if a position is within bounds and not a wall
    private boolean isValidPosition(int x, int y) {
        boolean inBoundary = x >= 0 && x < maze.getCols() && y >= 0 && y < maze.getRows();
        boolean isPath = maze.getGridAt(x, y) != '#';
        return inBoundary && isPath;
    }

    // attempts to move forward in current direction
    private boolean moveForward() {
        int nextX = currentPos[0];
        int nextY = currentPos[1];

        if (currentPos[0] != end[0] || currentPos[1] != end[1]) {
            if (direction == 0) {
                nextX++; // Move right
            } else if (direction == 1) {
                nextY++; // Move down
            } else if (direction == 2) {
                nextX--; // Move left
            } else if (direction == 3) {
                nextY--; // Move up
            }
        } else {
            logger.trace("Reached end!");
            return false; // stop moving if we've reached the end
        }

        if (isValidPosition(nextX, nextY)) {
            currentPos = new int[] { nextX, nextY }; // update position only if the move is valid
            moves.add("F"); // record the forward movement
            return true;
        }

        return false; // return false if we hit a wall or invalid position
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

    // implements right-hand rule maze solving algorithm
    public void exploreRightHandRule() { // explore the maze using the right-hand rule
        if (currentPos == null) {
            logger.error("No valid start point found in maze!");
        }

        logger.info("Starting right-hand rule exploration at position: ({}, {})", currentPos[0], currentPos[1]);

        while (!reachedEnd()) { // while the end is not reached
            if (canMoveRight()) { // if the right is valid, turn right and move forward
                turnRight();
                moveForward();
            } else if (canMoveForward()) { // if the forward is valid, move forward
                moveForward();
            } else if (canMoveLeft()) { // if the left is valid, turn left and move forward
                turnLeft();
                moveForward();
            } else { // for dead-ends, turn around and move forward (u-turns)
                turnAround();
                moveForward();
            }
        }

        logger.info("Explorer stopped at position: ({}, {})", currentPos[0], currentPos[1]);
        logger.info("Final moves: {}", String.join("", moves));
    }

    // validates and executes a sequence of moves from input
    public boolean solveMazeFromInput(String input) { // attempt to navigate maze using sequence of instructions
        int stepsTaken = 0; // tracks number of instructions executed
        if (input == null || input.isEmpty()) {
            logger.error("Input string is empty or null.");
            return false;
        }
        logger.info("Starting maze exploration from position: ({}, {})", currentPos[0], currentPos[1]);
        for (char instruction : input.toCharArray()) { // process each navigation command
            stepsTaken++;
            if (instruction == 'F') {
                if (!canMoveForward()) {
                    logger.warn("Hit a wall during move or out of bounds.");
                    break;
                }
                moveForward();
            } else if (instruction == 'R') {
                turnRight();
            } else if (instruction == 'L') {
                turnLeft();
            } else {
                logger.warn("Invalid move character encountered: {}", instruction);
                return false;
            }

            if (reachedEnd() && stepsTaken == input.length()) { // verify solution reaches end exactly
                logger.info("Maze solved! Reached the end at position: ({}, {})", currentPos[0], currentPos[1]);
                return true;
            } else if (reachedEnd() && stepsTaken != input.length()) { // detect if path continues past maze exit
                logger.warn("Reached end but path is still continuing!");
                return false;
            }
        }
        logger.warn("Finished processing input, but did not reach the end. End position is: ({},{})", currentPos[0],
                currentPos[1]);
        return false;
    }

    private boolean reachedEnd() {
        return currentPos[0] == end[0] && currentPos[1] == end[1];
    }

    // helper methods for checking possible moves in different directions
    private boolean canMoveForward() {
        int newX = currentPos[0];
        int newY = currentPos[1];

        if (direction == 0) {
            newX++; // Move right
        } else if (direction == 1) {
            newY++; // Move down
        } else if (direction == 2) {
            newX--; // Move left
        } else if (direction == 3) {
            newY--; // Move up
        }
        return isValidMove(newX, newY);
    }

    private boolean canMoveRight() {
        direction = (direction + 1) % 4; // temporarily rotate right to check that direction
        boolean canMove = canMoveForward();
        direction = (direction + 3) % 4; // rotate back to original direction
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
