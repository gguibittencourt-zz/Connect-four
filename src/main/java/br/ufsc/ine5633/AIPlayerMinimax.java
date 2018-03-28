package br.ufsc.ine5633;

import java.util.*;

import static java.lang.Math.min;

/**
 * br.ufsc.ine5633.AIPlayer using Minimax algorithm
 */
public class AIPlayerMinimax extends AIPlayer {

    private int evalueteCount = 0;
    private int nodesWon = 0;
    private Collection<Integer> allNodes;

    /**
     * Constructor with the given game board
     */
    public AIPlayerMinimax(Board board, Seed mySeed) {
        super(board, mySeed);
        this.allNodes = new ArrayList<>();
    }

    /**
     * Get next best move for computer. Return column to move
     */
    @Override
    int move() {
        this.addSeedsCount();
        int[] result = minimax(this.getDepth(), mySeed, Integer.MIN_VALUE, Integer.MAX_VALUE);

        System.out.println("\n\nForam avaliados " + this.evalueteCount + " nodos");
        this.allNodes.add(evalueteCount);
        this.evalueteCount = 0;
        System.out.println(this.nodesWon + " nodos eram vitória");
        this.nodesWon = 0;
        System.out.println("Posicao jogada --> " + (result[1] + 1));

        // depth, max-turn, alpha, beta
        return result[1];   // col
    }

    public void printFinalAverageNodes() {
        long allNodes = (long) this.allNodes.stream()
                .mapToDouble(n -> n)
                .average().getAsDouble();
        System.out.println(String.format("\nMédia do total de iterações: %d", allNodes));
    }

    /**
     * Recursive minimax at level of depth for either maximizing or minimizing player.
     * Return int[3] of {score, row, col}
     */
    private int[] minimax(int depth, Seed player, int alpha, int beta) {
        // Generate possible next moves in a list of int[2] of {row, col}.
        Collection<Coordinate> nextMoves = generateMoves();

        // mySeed is maximizing; while oppSeed is minimizing
        int score;
        int bestCol = -1;

        if (nextMoves.isEmpty() || depth == 0) {
            // Gameover or depth reached, evaluate score
            score = ((this.depth - depth) * 5);
            if (player == this.mySeed) {
                score *= -1;
            }
            score += evaluate();
            return new int[]{score, bestCol};
        } else {
            for (Coordinate move : nextMoves) {
                // try this move for the current "player"
                Cell nextCell = this.board.getCell(move);
                nextCell.setContent(player);
                this.board.setLastMovement(nextCell);
                if (player == mySeed) {  // mySeed (computer) is maximizing player
                    score = minimax(depth - 1, oppSeed, alpha, beta)[0];
                    if (score > alpha) {
                        alpha = score;
                        bestCol = move.getCoordX();
                    }
                } else {  // oppSeed is minimizing player
                    score = minimax(depth - 1, mySeed, alpha, beta)[0];
                    if (score < beta) {
                        beta = score;
                        bestCol = move.getCoordX();
                    }
                }
                // undo move
                nextCell.setContent(Seed.EMPTY);
                // cut-off
                if (alpha >= beta) break;
            }
            return new int[]{(player == mySeed) ? alpha : beta, bestCol};
        }
    }

    /**
     * Find all valid next moves.
     * Return Set of moves in int[2] of {row, col} or empty set if gameover
     */
    private Collection<Coordinate> generateMoves() {
        Collection<Coordinate> nextMoves = new LinkedList<>(); // allocate List

        // If gameover, i.e., no next move
        if (this.board.hasWon()) {
            this.nodesWon++;
            return nextMoves;   // return empty list
        }

        int centralCol = Double.valueOf(Math.floor(this.COLS / 2)).intValue();
        int distanceFromCenter = 1;

        // verifica e adiciona coluna central
        addMoveIfFirstIsEmpty(nextMoves, centralCol);

        // verifica e adiciona colunas perifericas a partir do centro
        while (distanceFromCenter <= centralCol) {
            int col = centralCol - distanceFromCenter; // col antes
            addMoveIfFirstIsEmpty(nextMoves, col);
            col = centralCol + distanceFromCenter; // col depois
            addMoveIfFirstIsEmpty(nextMoves, col);

            distanceFromCenter++;
        }
        return nextMoves;
    }

    private void addMoveIfFirstIsEmpty(Collection<Coordinate> nextMoves, int col) {
        Cell firstEmptyCel = this.getFirstEmptyCell(this.board.getColumn(col));
        if (firstEmptyCel != null) {
            nextMoves.add(firstEmptyCel.getPosition());
        }
    }

    private int evaluate() {
        this.evalueteCount++;
        int score = 0;
        score += calculaHeuristicaHorizontal();
        score += calculaHeuristicaVertical();
        score += calculaHeuristicaDiagonalEsquerda();
        score += calculaHeuristicaDiagonalDireita();
        return score;
    }

    private int calculaHeuristicaHorizontal() {
        int score = 0;
        for (int rowIdx = 0; rowIdx < this.ROWS; rowIdx++) {
            Cell[] row = board.getRow(rowIdx);

            int columnIdx = 0;
            while (columnIdx < (this.COLS - 3)) {
                Cell[] setOfFour = Arrays.copyOfRange(row, columnIdx, columnIdx + 4);
                int mySeedsCount = countSeedOfType(setOfFour, mySeed);
                int oppSeedsCount = countSeedOfType(setOfFour, oppSeed);

                int firstEmpty = getNextEmptyIndex(setOfFour);
                int firstDifferent = setOfFour.length;
                if (oppSeedsCount == 0) {
                    int emptySeedsCount = countSeedOfType(setOfFour, Seed.EMPTY);
                    score += getScore(mySeed, mySeedsCount, emptySeedsCount);
                } else if (mySeedsCount == 0) {
                    int emptySeedsCount = countSeedOfType(setOfFour, Seed.EMPTY);
                    score += getScore(oppSeed, oppSeedsCount, emptySeedsCount);
                } else {
                    firstDifferent = getNextDifferentIndex(setOfFour);
                }
                columnIdx += min(firstEmpty, firstDifferent);
            }
        }
        return score;
    }

    private int countSeedOfType(Cell[] setOfFour, Seed mySeed) {
        Long count = Arrays.stream(setOfFour)
                .filter(cell -> cell.getContent().equals(mySeed))
                .count();
        return count.intValue();
    }

    private int getScoreFromGroupComparation(Group group1, Group group2) {
        Seed seed1 = group1.getSeed();
        Seed seed2 = group2.getSeed();
        int group2Score = group2.getGroupScore();
        int group1Score = group1.getGroupScore();
        if (seed1 == seed2 && seed1 != Seed.EMPTY) {
            if (seed1 == this.mySeed) {
                return group1Score >= group2Score ? group1Score : group2Score; //Se for minha seed escolhe o melhor valor
            } else {
                return group1Score <= group2Score ? group1Score : group2Score; //Se for seed do adversario escolhe o pior valor
            }
        } else {
            return group1Score + group2Score;
        }

    }

    /**
     * Analysis the board, and return the score for vertical analysis only.
     *
     * @return The Score calculated for vertical analysis. If positive refers a good state for player, otherwise, it's good for opponent
     */
    private int calculaHeuristicaVertical() {
        int score = 0;
        int lastRow = this.ROWS - 1;
        for (int colNumber = 0; colNumber < this.COLS; colNumber++) {
            Cell[] col = this.board.getColumn(colNumber);
            int totalEmptyCells = 0;
            int countCells = 0;
            int rowNumber = lastRow;
            Seed cellSeed = Seed.EMPTY;
            boolean stop = false;
            while (rowNumber > -1 && !stop) {
                cellSeed = col[rowNumber].getContent();
                if (cellSeed != Seed.EMPTY) {
                    totalEmptyCells = lastRow - rowNumber;
                    countCells++;
                    for (int temp = rowNumber - 1; temp > -1 && !stop; temp--) {
                        if (col[temp].getContent() == cellSeed) {
                            countCells++;
                        } else {
                            stop = true;
                        }
                    }
                }
                rowNumber--;
            }
            score += getScore(cellSeed, countCells, totalEmptyCells);
        }
        return score;
    }

    private int calculaHeuristicaDiagonalDireita() {
        int score = 0;
        for (int groupCount = 0; groupCount < 3; groupCount++) {
            int totalGroups = 3 - groupCount;
            //Parte superior
            Group[] groupsUp = new Group[totalGroups];

            //Parte inferior
            Group[] groupsDown = new Group[totalGroups];

            for (int group = 0; group < totalGroups; group++) {
                groupsUp[group] = getDiagonalDireitaGroup(group, groupCount + group);
                groupsUp[group].setGroupScore(this.getScore(groupsUp[group]));
                groupsDown[group] = getDiagonalDireitaGroup(groupCount + group + 1, group);
                groupsDown[group].setGroupScore(this.getScore(groupsDown[group]));
            }
            score = calculateScore(score, totalGroups, groupsUp, groupsDown);
        }
        return score;
    }

    private int calculaHeuristicaDiagonalEsquerda() {
        int score = 0;
        for (int groupCount = this.COLS - 1; groupCount > 3; groupCount--) {
            int totalGroups = groupCount - 3;
            //Parte superior
            Group[] groupsUp = new Group[totalGroups];

            //Parte inferior
            Group[] groupsDown = new Group[totalGroups];

            for (int group = 0; group < totalGroups; group++) {
                groupsUp[group] = getDiagonalEsquerdaGroup(groupCount - group, group);
                groupsUp[group].setGroupScore(this.getScore(groupsUp[group]));
                groupsDown[group] = getDiagonalEsquerdaGroup(groupCount - group - 1, group);
                groupsDown[group].setGroupScore(this.getScore(groupsDown[group]));
            }
            score = calculateScore(score, totalGroups, groupsUp, groupsDown);
        }
        return score;
    }

    private int calculateScore(int score, int totalGroups, Group[] groupsUp, Group[] groupsDown) {
        if (totalGroups == 1) {
            score += groupsUp[0].getGroupScore();
            score += groupsDown[0].getGroupScore();
        } else {
            score += this.getScoreFromGroupComparation(groupsUp[0], groupsUp[1]);
            score += this.getScoreFromGroupComparation(groupsDown[0], groupsDown[1]);
            if (totalGroups == 3) {
                score += groupsUp[2].getGroupScore();
                score += groupsDown[2].getGroupScore();
            }
        }
        return score;
    }

    private Group getDiagonalDireitaGroup(int colBegin, int rowBegin) {
        int countEmptys = 0;
        int countSeeds = 0;
        boolean stop = false;
        Seed cellSeed = Seed.EMPTY;
        for (int i = 0; i < 4 && !stop; i++) {
            Seed nextCellSeed = this.board.getCell(colBegin + i, rowBegin + i).getContent();
            if (nextCellSeed == Seed.EMPTY) {
                countEmptys++;
            } else {
                if (cellSeed == Seed.EMPTY) {
                    cellSeed = nextCellSeed;
                    countSeeds++;
                } else if (cellSeed == nextCellSeed) {
                    countEmptys++;
                } else {
                    stop = true;
                }
            }
        }

        return new Group(cellSeed, countSeeds, countEmptys);
    }

    private Group getDiagonalEsquerdaGroup(int colBegin, int rowBegin) {
        int countEmptys = 0;
        int countSeeds = 0;
        boolean stop = false;
        Seed cellSeed = Seed.EMPTY;
        for (int i = 0; i < 4 && !stop; i++) {
            Seed nextCellSeed = this.board.getCell(colBegin - i, rowBegin + i).getContent();
            if (nextCellSeed == Seed.EMPTY) {
                countEmptys++;
            } else {
                if (cellSeed == Seed.EMPTY) {
                    cellSeed = nextCellSeed;
                    countSeeds++;
                } else if (cellSeed == nextCellSeed) {
                    countEmptys++;
                } else {
                    stop = true;
                }
            }
        }

        return new Group(cellSeed, countSeeds, countEmptys);
    }

    private int getScore(Seed cellSeed, int countSeeds, int countEmptys) {
        int score = 0;
        if (countSeeds > 0) {
            if (countSeeds == 1 && countEmptys >= 3) {
                score += 10;
            } else if (countSeeds == 2 && countEmptys >= 2) {
                score += 75;
            } else if (countSeeds == 3 && countEmptys >= 1) {
                score += 700;
            } else if (countSeeds == 4) {
                score += 10000;
            }
        }
        if (cellSeed != this.mySeed) {
            score *= -1;
        }

        return score;
    }

    private int getScore(Group group) {
        return this.getScore(group.getSeed(), group.getCountSeeds(), group.getCountEmptys());
    }

    private Cell getFirstEmptyCell(Cell[] col) {
        for (Cell aCol : col) {
            if (aCol.getContent() == Seed.EMPTY) {
                return aCol;
            }
        }
        return null;
    }

    private int getNextDifferentIndex(Cell[] col) {
        Seed baseSeed = col[getFirstNotEmptyIndex(col)].getContent();
        for (int i = 1; i < col.length; i++) {
            if (col[i].getContent() != Seed.EMPTY && col[i].getContent() != baseSeed) {
                return i;
            }
        }
        return col.length;
    }

    private int getFirstNotEmptyIndex(Cell[] row) {
        for (int i = 0; i < row.length; i++) {
            if (row[i].getContent() == Seed.PLAYER_1 || row[i].getContent() == Seed.PLAYER_2) {
                return i;
            }
        }
        return row.length;
    }

    private int getNextEmptyIndex(Cell[] row) {
        for (int i = 1; i < row.length; i++) {
            if (row[i].getContent() == Seed.EMPTY) {
                return i;
            }
        }
        return row.length;
    }
}
