package br.ufsc.ine5633;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

import static br.ufsc.ine5633.GameProperties.COLS;
import static br.ufsc.ine5633.GameProperties.ROWS;
import static java.lang.String.format;

public class Main {

    private static final Board BOARD = new Board(COLS, ROWS);
    private static final Scanner SCAN = new Scanner(System.in);

    public static void main(String[] args) {
        Boolean hasWinner = false;
        Boolean hasPrunning = selectGameMode(SCAN);
        Boolean humanFirst = readFirstPlayer(SCAN);

        AIPlayerMinimax IA = new AIPlayerMinimax(BOARD, Seed.PLAYER_2, hasPrunning);

        Seed player = humanFirst ? Seed.PLAYER_1 : Seed.PLAYER_2;

        printBoard(BOARD);
        while (!hasWinner) {
            Integer chosenColumn;
            if (player == Seed.PLAYER_1) {
                chosenColumn = readColumn(SCAN);
                BOARD.addInColumn(chosenColumn - 1, player);
                player = Seed.PLAYER_2;
            } else {
                chosenColumn = IA.move();
                BOARD.addInColumn(chosenColumn, player);
                player = Seed.PLAYER_1;
            }
            printBoard(BOARD);
            hasWinner = BOARD.hasWon();
        }

        IA.printFinalAverageNodes();

        System.out.println(String.format("\n%s venceu!", player.equals(Seed.PLAYER_2) ? "Você" : "O computador"));
        SCAN.close();
    }

    private static Integer readColumn(Scanner scan) {
        String input = null;
        boolean valid = false;

        while (input == null || !valid) {
            System.out.print(GameProperties.MSG_CHOOSE_COLUMN);
            input = scan.next();

            valid = input.matches("[1-7]");
            if (!valid) {
                System.out.println(GameProperties.MSG_INVALID);
            }
        }
        return Integer.valueOf(input);
    }


    private static Boolean selectGameMode(Scanner scan) {
        String input = null;
        Boolean noPrunning = false;
        Boolean hasPrunning = false;

        while (input == null || (!noPrunning && !hasPrunning)) {
            System.out.print(GameProperties.MSG_GAME_MODE);
            input = scan.next();

            noPrunning = input.matches("^1$");
            hasPrunning = input.matches("^2$");
            if (!noPrunning && !hasPrunning) {
                System.out.println(GameProperties.MSG_INVALID);
            }
        }
        return hasPrunning;
    }

    private static Boolean readFirstPlayer(Scanner scan) {
        String input = null;
        boolean isTrue = false;
        boolean isFalse = false;

        while (input == null || (!isTrue && !isFalse)) {
            System.out.print(GameProperties.MSG_FIRST_PLAYER);
            input = scan.next();

            isTrue = input.matches("s|S");
            isFalse = input.matches("n|N");
            if (!isTrue && !isFalse) {
                System.out.println(GameProperties.MSG_INVALID);
            }
        }
        return isTrue;
    }

    private static void printBoard(Board board) {
        String[] linesStr = new String[ROWS];
        printNumbers();
        for (int x = 0; x < board.getCells().length; x++) {
            Cell[] column = board.getCells()[x];
            for (int y = 0; y < column.length; y++) {
                String end = x == (board.getCells().length - 1) ? "|" : "";
                String line = linesStr[column.length - 1 - y];
                line = line != null ? line : "";
                linesStr[column.length - 1 - y] = format("%s|%s%s", line, column[y].getContent().getValue(), end);
            }
        }

        Arrays.stream(linesStr).forEach(System.out::println);
        printNumbers();
    }

    private static void printNumbers() {
        IntStream.rangeClosed(1, COLS)
                .forEach(value -> System.out.print(format(" %d", value)));
        System.out.println();
    }
}
