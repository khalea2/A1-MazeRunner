package ca.mcmaster.se2aa4.mazerunner;

import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        logger.info("** Starting Maze Runner");
        System.out.println("Starting Maze Runner");

        InputHandler inputHandler = new InputHandler();

        if (!inputHandler.parseArgs(args)) {
            logger.error("Failed to parse command-line arguments.");
            return;

        }

        String inputFilePath = inputHandler.getInputFilePath();
        String inputPath = inputHandler.getMazePath();

        if (inputFilePath == null) {
            logger.error("Failed to read File Path.");
            return;
        }

        Maze maze = new Maze(inputFilePath);
        Explorer explorer = new Explorer(maze);

        if (inputPath == null) {
            logger.info("No path provided.");
        } else {
            System.out.println("Solving maze with path");
            System.out.println("Starting maze at: " + Arrays.toString(maze.getLeftOpening()));
            Path path = new Path(inputPath);

            System.out.println("Inputted canonical path: " + path.getFormattedInputtedPath());
            System.out.println("Inputted factorized path: " + path.getFactorizedInputtedPath());

            MazeValidator mazeValidator = new MazeValidator(maze, explorer, path);
            if (mazeValidator.getIsValid()) {
                System.out.println("Maze solved successfully with inputted path.");
            } else {
                System.out.println("Maze not solved with inputted path!");
                System.out.println("Maze runner stopped at: " + Arrays.toString(explorer.getCurrentPosition()));
            }
            return;
        }

        try {
            System.out.println("Starting maze at: " + Arrays.toString(maze.getLeftOpening()));
            System.out.println("Starting right hand rule");
            logger.info("**** Computing path");

            explorer.exploreRightHandRule();
            List<String> moves = explorer.getPathSteps();
            Path path = new Path(moves);

            System.out.println("Maze solved!");
            System.out.println("Final canonical path: " + path.getFormattedPath());
            System.out.println("Final factorized path: " + path.getFactorizedPath());

        } catch (Exception e) {
            logger.error("/!\\ An error has occurred /!\\ Error:{}", e.getMessage());
        }

        logger.info("** End of MazeRunner");
    }
}
