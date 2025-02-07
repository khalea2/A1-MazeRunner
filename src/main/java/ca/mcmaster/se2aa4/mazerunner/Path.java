package ca.mcmaster.se2aa4.mazerunner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Path {
    private static final Logger logger = LogManager.getLogger();
    private StringBuilder path;

    public Path(String initialPath) {
        path = new StringBuilder();
        path.append(initialPath);
        logger.info("Initialized path: {}", initialPath);
    }
}
