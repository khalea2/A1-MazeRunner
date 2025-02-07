package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InputHandler {
    private static final Logger logger = LogManager.getLogger(InputHandler.class);

    private CommandLine cmdArgs;
    private final Options cliOpts;

    public InputHandler() {
        cliOpts = new Options();
        cliOpts.addOption("i", "input", true, "Path to the maze input file");
    }

    public boolean parseArgs(String[] args) {
        CommandLineParser argsParser = new DefaultParser();
        try {
            cmdArgs = argsParser.parse(cliOpts, args);
            return true;
        } catch (ParseException e) {
            logger.error("Error parsing arguments: {}", e.getMessage());
            return false;
        }
    }

    public String getInputFilePath() {
        if (cmdArgs != null && cmdArgs.hasOption("i")) {
            return cmdArgs.getOptionValue("i");
        } else {
            return null;
        }
    }
}
