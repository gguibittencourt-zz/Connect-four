package br.ufsc.ine5633;

/**
 * Abstract superclass for all AI players with different strategies.
 * To construct an AI player:
 * 1. Construct an instance (of its subclass) with the game br.ufsc.ine5633.Board
 * 2. Call setSeed() to set the computer's seed
 * 3. Call move() which returns the next move in an int[2] array of {row, col}.
 * <p>
 * The implementation subclasses need to override abstract method move().
 * They shall not modify br.ufsc.ine5633.Cell[][], i.e., no side effect expected.
 * Assume that next move is available, i.e., not game-over yet.
 */
public abstract class AIPlayer {
    protected int ROWS = GameMain.ROWS;  // number of rows
    protected int COLS = GameMain.COLS;  // number of columns
    protected int depth = GameMain.DEPTH;  // profundidade para investigar
    protected int countSeeds = 0;

    protected Board board; // the game board
    protected Seed mySeed;    // computer's seed
    protected Seed oppSeed;   // opponent's seed

    /**
     * Constructor with reference to game board
     */
    public AIPlayer(Board board, Seed mySeed) {
        this.board = board;
        this.setSeed(mySeed);
    }

    /**
     * Set/change the seed used by computer and opponent
     */
    public void setSeed(Seed seed) {
        this.mySeed = seed;
        oppSeed = (mySeed == Seed.PLAYER_1) ? Seed.PLAYER_2 : Seed.PLAYER_1;
    }

    /**
     * Abstract method to get next move. Return column to move
     */
    abstract int move();  // to be implemented by subclasses

    protected void addSeedsCount() {
        this.countSeeds++;
    }

    protected int getDepth() {
        return this.depth + (this.countSeeds / 4) < 10 ? this.depth + (this.countSeeds / 4) : 10;
    }
}