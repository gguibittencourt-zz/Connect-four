package br.ufsc.ine5633;

public abstract class AIPlayer {
    protected int ROWS = GameProperties.ROWS;  // number of rows
    protected int COLS = GameProperties.COLS;  // number of columns
    protected int depth = GameProperties.DEPTH;  // profundidade para investigar
    protected int countSeeds = 0;

    protected Board board; // the game board
    protected Seed mySeed;    // computer's seed
    protected Seed oppSeed;   // opponent's seed

    public AIPlayer(Board board, Seed mySeed) {
        this.board = board;
        this.setSeed(mySeed);
    }

    public void setSeed(Seed seed) {
        this.mySeed = seed;
        oppSeed = (mySeed == Seed.PLAYER_1) ? Seed.PLAYER_2 : Seed.PLAYER_1;
    }

    abstract int move();  // to be implemented by subclasses

    protected void addSeedsCount() {
        this.countSeeds++;
    }

    protected int getDepth() {
        return this.depth + (this.countSeeds / 4) < 10 ? this.depth + (this.countSeeds / 4) : 10;
    }
}