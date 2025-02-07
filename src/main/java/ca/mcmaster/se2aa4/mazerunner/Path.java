package ca.mcmaster.se2aa4.mazerunner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private static final Logger logger = LogManager.getLogger();
    private List<String> path;
    private List<String> inputtedPath;

    private String normalizedPath;
    private String standardizedPath;
    private String factorizedPath;
    private String factorizedInputtedPath;

    public Path(List<String> path) {
        this.path = path;

        normalizedPath = formatMoves(this.path);
        factorizedPath = factorizeMoves(this.path);

        logger.info("Factorized path: {}", factorizedPath);
        logger.info("Canonical path: {}", normalizedPath);
    }

    public Path(String inputtedPath) {
        String input = convertFormat(inputtedPath);
        this.inputtedPath = pathToList(input);

        standardizedPath = formatMoves(this.inputtedPath);
        factorizedInputtedPath = factorizeMoves(this.inputtedPath);

        logger.info("Factorized inputted path: {}", factorizedInputtedPath);
        logger.info("Canonical inputted path: {}", standardizedPath);
    }

    public Path(List<String> path, String inputtedPath) {
        this.path = path;

        String input = convertFormat(inputtedPath);
        this.inputtedPath = pathToList(input);

        normalizedPath = formatMoves(this.path);
        factorizedPath = factorizeMoves(this.path);
        standardizedPath = formatMoves(this.inputtedPath);
        factorizedInputtedPath = factorizeMoves(this.inputtedPath);

        logger.info("Initialized path: {}", factorizedPath);
        logger.info("Canonical path: {}", normalizedPath);

        logger.info("Initialized inputted path: {}", factorizedInputtedPath);
        logger.info("Canonical inputted path: {}", standardizedPath);
    }

    private List<String> pathToList(String path) {
        List<String> pathList = new ArrayList<>();

        for (char move : path.toCharArray()) {
            pathList.add(String.valueOf(move));
        }
        return pathList;
    }

    private String formatMoves(List<String> moves) {
        if (moves.isEmpty())
            return "";

        StringBuilder formattedMoves = new StringBuilder();
        String lastMove = moves.getFirst();
        int count = 1;

        for (int i = 1; i < moves.size(); i++) {
            if (moves.get(i).equals(lastMove)) {
                count++;
            } else {
                formattedMoves.append(lastMove.repeat(count)).append(" ");
                lastMove = moves.get(i);
                count = 1;
            }
        }
        formattedMoves.append(lastMove.repeat(count));

        return formattedMoves.toString().trim();
    }

    private String factorizeMoves(List<String> path) {
        if (path.isEmpty())
            return "";

        StringBuilder factorizedPath = new StringBuilder();
        String lastMove = path.getFirst();
        int count = 1;

        for (int i = 1; i < path.size(); i++) {
            if (path.get(i).equals(lastMove)) {
                count++;
            } else {
                factorizedPath.append(count > 1 ? count : "").append(lastMove).append(" ");
                lastMove = path.get(i);
                count = 1;
            }
        }
        factorizedPath.append(count > 1 ? count : "").append(lastMove);

        return factorizedPath.toString().trim();
    }

    private String convertFormat(String input) {
        StringBuilder regularPath = new StringBuilder();
        StringBuilder numberBuffer = new StringBuilder();

        String[] parts = input.split("\\s+");

        for (String part : parts) {
            for (int i = 0; i < part.length(); i++) {
                char currentChar = part.charAt(i);

                if (Character.isDigit(currentChar)) {
                    numberBuffer.append(currentChar);
                } else {

                    if (!numberBuffer.isEmpty()) {
                        int repeatCount = Integer.parseInt(numberBuffer.toString());
                        regularPath.append(String.valueOf(currentChar).repeat(repeatCount));
                        numberBuffer.setLength(0);
                    } else {
                        regularPath.append(currentChar);
                    }
                }
            }

            if (!numberBuffer.isEmpty()) {
                int repeatCount = Integer.parseInt(numberBuffer.toString());
                regularPath.append(String.valueOf(part.charAt(part.length() - 1)).repeat(repeatCount));
                numberBuffer.setLength(0);
            }
        }

        return regularPath.toString();
    }

    public String getFormattedPath() {
        return normalizedPath;
    }

    public String getFormattedInputtedPath() {
        return standardizedPath;
    }

    public String getFactorizedPath() {
        return factorizedPath;
    }

    public String getFactorizedInputtedPath() {
        return factorizedInputtedPath;
    }

}