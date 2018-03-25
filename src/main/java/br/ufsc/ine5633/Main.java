package br.ufsc.ine5633;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

import static br.ufsc.ine5633.GameMain.COLS;
import static br.ufsc.ine5633.GameMain.ROWS;
import static java.lang.String.format;

public class Main {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Board board = new Board(COLS, ROWS);
        AIPlayerMinimax IA = new AIPlayerMinimax(board, Seed.PLAYER_2);
        Boolean hasWinner = false;

        Boolean humanFirst = readFirstPlayer(scan);
        Seed player = humanFirst ? Seed.PLAYER_1 : Seed.PLAYER_2;

        printBoard(board);
        while (!hasWinner) {
            Integer chosenColumn;
            if (player == Seed.PLAYER_1) {
                chosenColumn = readColumn(scan);
                board.addInColumn(chosenColumn - 1, player);
                player = Seed.PLAYER_2;
            } else {
                chosenColumn = IA.move();
                board.addInColumn(chosenColumn, player);
                player = Seed.PLAYER_1;
            }
            printBoard(board);
            hasWinner = board.hasWon();
        }

        System.out.println(String.format("\n%s venceu!", player.equals(Seed.PLAYER_2) ? "Você" : "O computador"));
        scan.close();
    }

    private static Integer readColumn(Scanner scan) {
        String input = null;
        boolean valid = false;

        while (input == null || !valid) {
            System.out.print(GameMain.MSG_CHOOSE_COLUMN);
            input = scan.next();

            valid = input.matches("[1-7]");
            if (!valid) {
                System.out.println(GameMain.MSG_INVALID);
            }
        }
        return Integer.valueOf(input);
    }

    private static Boolean readFirstPlayer(Scanner scan) {
        String input = null;
        boolean isTrue = false;
        boolean isFalse = false;

        while (input == null || (!isTrue && !isFalse)) {
            System.out.print(GameMain.MSG_FIRST_PLAYER);
            input = scan.next();

            isTrue = input.matches("s|S");
            isFalse = input.matches("n|N");
            if (!isTrue && !isFalse) {
                System.out.println(GameMain.MSG_INVALID);
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
