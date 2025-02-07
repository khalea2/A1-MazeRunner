package ca.mcmaster.se2aa4.mazerunner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MazeValidator { // Class to validate the maze with the provided path

    private static final Logger logger = LogManager.getLogger();
    private Maze maze;
    private Explorer explorer;
    private Path path;
    private boolean isValid;

    public MazeValidator(Maze maze, Explorer explorer, Path path) { // Constructor to initialize the maze, explorer, and
                                                                    // path
        this.maze = maze;
        this.explorer = explorer;
        this.path = path;
        validateMazeWithPath();
    }

    private void validateMazeWithPath() { // Method to validate the maze with the provided path
        String inputPath = path.getFormattedInputtedPath();

        logger.info("Starting maze validation with path: {}", inputPath);

        // Use the path to attempt solving the maze
        isValid = explorer.solveMazeFromInput(inputPath); // Solve the maze with the provided path

        if (isValid) {
            logger.info("Maze successfully solved with the provided path.");
        } else {
            logger.warn("Maze was not solved with the provided path.");
        }
    }

    public boolean getIsValid() { // Method to return the validity of the maze
        return isValid;
    }
}