package ca.mcmaster.se2aa4.mazerunner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        logger.info("** Starting Maze Runner");

        InputHandler inputHandler = new InputHandler();

        if (!inputHandler.parseArgs(args)) {
            logger.error("Failed to parse command-line arguments.");
            return;
        }

        String inputFilePath = inputHandler.getInputFilePath();
        if (inputFilePath == null) {
            return;
        }

        try {
            Maze maze = new Maze(inputFilePath);
            Explorer explorer = new Explorer(maze);

            logger.info("**** Computing path");
            // explorer.exploreMaze();
            explorer.exploreRightHandRule();

            // logger.warn("PATH NOT COMPUTED");
        } catch (Exception e) {
            logger.error("/!\\ An error has occurred /!\\");
        }
        logger.info("** End of MazeRunner");
    }
}
