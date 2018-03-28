package br.ufsc.ine5633;

import static br.ufsc.ine5633.GameMain.COLS;
import static br.ufsc.ine5633.GameMain.ROWS;

public class Board {

    private Cell[][] cells;
    private Cell lastMovement;

    public Board(int nColumns, int nLines) {
        this.cells = new Cell[nColumns][nLines];
        for (int x = 0; x < cells.length; x++) {
            Cell[] column = cells[x];
            for (int y = 0; y < column.length; y++) {
                cells[x][y] = new Cell(x, y);
            }
        }
        this.lastMovement = null;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public Cell getCell(Coordinate coordinate) {
        return this.cells[coordinate.getCoordX()][coordinate.getCoordY()];
    }

    public Cell getCell(int col, int row) {
        return this.cells[col][row];
    }

    public Cell[] getColumn(int colNumber) {
        return this.cells[colNumber];
    }

    public Cell[] getRow(int rowNumber) {
        Cell[] line = new Cell[COLS];
        for (int c = 0; c < COLS; c++) {
            line[c] = this.cells[c][rowNumber];
        }
        return line;
    }

    public Cell getLastMovement() {
        return lastMovement;
    }

    public Board setLastMovement(Cell lastMovement) {
        this.lastMovement = lastMovement;
        return this;
    }

    public void addInColumn(Integer chosenColumn, Seed value) {
        Cell[] column = this.cells[chosenColumn];
        for (int rowIndex = 0; rowIndex < column.length; rowIndex++) {
            if (column[rowIndex].getContent() == Seed.EMPTY) {
                this.lastMovement = this.cells[chosenColumn][rowIndex].setContent(value);
                break;
            }
        }
    }

    /**
     * Returns true if thePlayer wins
     */
    public boolean hasWon() {
        Cell lastMovement = this.getLastMovement();
        if (lastMovement != null) {
            return this.checkWonVertical(lastMovement.getContent(), lastMovement.getPosition().getCoordX(), lastMovement.getPosition().getCoordY()) ||
                    this.checkWonHorizontal(lastMovement.getContent(), lastMovement.getPosition().getCoordX(), lastMovement.getPosition().getCoordY()) ||
                    this.checkWonDiagonalDireita(lastMovement.getContent(), lastMovement.getPosition().getCoordX(), lastMovement.getPosition().getCoordY()) ||
                    this.checkWonDiagonalEsquerda(lastMovement.getContent(), lastMovement.getPosition().getCoordX(), lastMovement.getPosition().getCoordY());
        } else {
            return false;
        }
    }

    private boolean checkWonHorizontal(Seed seed, int col, int row) {
        int begin = col - 4 > 0 ? col - 4 : 0;
        int end = col + 4 < COLS ? col + 4 : COLS;
        int countSeedsGrouped = 0;
        while (begin < end) {
            if (this.getCell(begin, row).getContent() == seed) {
                countSeedsGrouped++;
                if (countSeedsGrouped == 4) {
                    return true;
                }
            } else {
                countSeedsGrouped = 0;
            }
            begin++;
        }
        return false;
    }

    private boolean checkWonVertical(Seed seed, int col, int row) {
        if (row >= 3) {
            int begin = row;
            int end = row - 3;
            int countSeedsGrouped = 0;
            while (begin >= end) {
                if (this.getCell(col, begin).getContent() == seed) {
                    countSeedsGrouped++;
                    if (countSeedsGrouped == 4) {
                        return true;
                    }
                } else {
                    countSeedsGrouped = 0;
                }
                begin--;
            }
        }
        return false;
    }

    private boolean checkWonDiagonalDireita(Seed seed, int col, int row) {
        int countSeedsGrouped = 1;

        int colBegin = col - 1;
        int rowBegin = row - 1;
//Avaliacao a esquerda da peca jogada
        while (colBegin > -1 && rowBegin > -1) {
            if (this.getCell(colBegin, rowBegin).getContent() != seed) break;
            countSeedsGrouped++;
            colBegin--;
            rowBegin--;
        }

        colBegin = col + 1;
        rowBegin = row + 1;
//Avaliacao a direita da peca jogada
        while (colBegin < COLS && rowBegin < ROWS) {
            if (this.getCell(colBegin, rowBegin).getContent() != seed) {
                break;
            }
            countSeedsGrouped++;
            colBegin++;
            rowBegin++;
        }

        return countSeedsGrouped > 3;
    }

    private boolean checkWonDiagonalEsquerda(Seed seed, int col, int row) {
        int countSeedsGrouped = 1;

        int colBegin = col - 1;
        int rowBegin = row + 1;
//Avaliacao a esquerda da peca jogada
        while (colBegin > -1 && rowBegin < ROWS) {
            if (this.getCell(colBegin, rowBegin).getContent() != seed) break;
            countSeedsGrouped++;
            colBegin--;
            rowBegin++;
        }

        colBegin = col + 1;
        rowBegin = row - 1;
//Avaliacao a direita da peca jogada
        while (colBegin < COLS && rowBegin > -1) {
            if (this.getCell(colBegin, rowBegin).getContent() != seed) break;
            countSeedsGrouped++;
            colBegin++;
            rowBegin--;
        }

        return countSeedsGrouped > 3;
    }
}
