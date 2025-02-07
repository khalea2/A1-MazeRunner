package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// processes and validates command line arguments for the maze runner
public class InputHandler {
    private static final Logger logger = LogManager.getLogger(InputHandler.class);

    // stores parsed command line arguments
    private CommandLine cmdArgs;
    private final Options cliOpts;

    public InputHandler() {
        // initialize available command line options
        cliOpts = new Options();
        cliOpts.addOption("i", "input", true, "Path to the maze input file");
        cliOpts.addOption("p", "path", true, "Solve maze based on inputted path");
    }

    // attempts to parse command line arguments, returns success status
    public boolean parseArgs(String[] args) {
        CommandLineParser argsParser = new DefaultParser();
        try {
            cmdArgs = argsParser.parse(cliOpts, args); // attempt to parse args against defined options
            return true; // return true if parsing succeeds
        } catch (ParseException e) {
            logger.error("Error parsing arguments: {}", e.getMessage());
            return false; // return false if any parsing error occurs
        }
    }

    public String getInputFilePath() {
        if (cmdArgs != null && cmdArgs.hasOption("i")) {
            return cmdArgs.getOptionValue("i"); // extract maze file path from -i option
        } else {
            logger.error("No input file provided");
            return null; // return null if required input file option is missing
        }
    }

    public String getMazePath() {
        if (cmdArgs != null && cmdArgs.hasOption("p")) {
            return cmdArgs.getOptionValue("p");
        } else {
            logger.info("No path provided");
            return null;
        }
    }
}
