package ca.mcmaster.se2aa4.mazerunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.cli.*;

public class Main {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        logger.info("** Starting Maze Runner");

        Options cliOptions = new Options();
        cliOptions.addOption("i", "input", true, "Input to maze runner");

        CommandLineParser argsParser = new DefaultParser();
        HelpFormatter helpDisplay = new HelpFormatter();

        try {
            CommandLine cmdArgs = argsParser.parse(cliOptions, args);

            if (!cmdArgs.hasOption("i")) {
                logger.error("/!\\ Missing required -i flag for input file /!\\");
                helpDisplay.printHelp("MazeRunner", cliOptions);
                return;
            }

            String mazeFilePath = cmdArgs.getOptionValue("i");
            logger.info("**** Reading the maze from file: {}", mazeFilePath);

            BufferedReader fileReader = new BufferedReader(new FileReader(mazeFilePath));

            String currentLine;
            while ((currentLine = fileReader.readLine()) != null) {
                StringBuilder lineOutput = new StringBuilder();
                for (int position = 0; position < currentLine.length(); position++) {
                    if (currentLine.charAt(position) == '#') {
                        lineOutput.append("WALL ");
                    } else if (currentLine.charAt(position) == ' ') {
                        lineOutput.append("PASS ");
                    }
                }
                logger.trace(lineOutput.toString());
            }
            fileReader.close();

        } catch (Exception e) {
            logger.error("/!\\ An error has occurred /!\\");
        }
        logger.info("**** Computing path");
        logger.warn("PATH NOT COMPUTED");
        logger.info("** End of MazeRunner");
    }
}
